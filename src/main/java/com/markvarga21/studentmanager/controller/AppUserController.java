package com.markvarga21.studentmanager.controller;

import com.markvarga21.studentmanager.dto.UserLogin;
import com.markvarga21.studentmanager.entity.AppUser;
import com.markvarga21.studentmanager.exception.InvalidUserCredentialsException;
import com.markvarga21.studentmanager.service.auth.AppUserService;
import com.markvarga21.studentmanager.service.auth.TokenManagementService;
import com.markvarga21.studentmanager.service.auth.webtoken.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

/**
 * The controller for the user authentication.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
@CrossOrigin
@Tag(
    name = "User controller",
    description = "A controller used to manipulate users in the app."
)
public class AppUserController {
    /**
     * The user service.
     */
    private final AppUserService appUserService;

    /**
     * The {@code PasswordEncoder} object.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * The {@code AuthenticationManager} object.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * The {@code JwtService} object for creating JWT tokens.
     */
    private final JwtService jwtService;

    /**
     * The {@code UserDetailsService} object.
     */
    private final UserDetailsService userDetailsService;

    /**
     * The {@code TokenManagementService} object.
     */
    private final TokenManagementService tokenManagementService;

    /**
     * The {@code LogoutSuccessHandler} object.
     */
    private final LogoutSuccessHandler logoutSuccessHandler;

    /**
     * Endpoint for fetching all users.
     *
     * @param page The page number.
     * @param size The size of elements inside a single page.
     * @return A page of the users.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Fetches all users from the database.")
    @GetMapping("/users")
    public Page<AppUser> fetchUsers(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "10") final Integer size
    ) {
        return appUserService.getAllUsers(page, size);
    }

    /**
     * Endpoint for deleting a user.
     *
     * @param id The id of the user.
     * @return A descriptive message of the deletion.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Deletes a user from the database.")
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable final Long id) {
        return appUserService.deleteUserById(id);
    }

    /**
     * Endpoint for registering a user.
     *
     * @param user The user object.
     * @return The registered user object.
     */
    @Operation(summary = "Registers a user.")
    @PostMapping("/register")
    public ResponseEntity<AppUser> registerUser(
            @RequestBody @Valid final AppUser user
    ) {
        if (user.getRoles() != null) {
            String message = "Roles cannot be set during registration.";
            log.error(message);
            throw new InvalidUserCredentialsException(message);
        }
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(appUserService.registerUser(user));
    }

    /**
     * Endpoint for logging in a user.
     *
     * @param user The user object.
     * @return The created JWT token.
     */
    @Operation(summary = "Logs in a user.")
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(
            @RequestBody @Valid final UserLogin user
    ) {
        Authentication auth = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                ));
        if (!auth.isAuthenticated()) {
            log.error("Invalid user credentials.");
            throw new InvalidUserCredentialsException("Invalid user credentials.");
        }
        String token = this.jwtService.generateJwtToken(
                this.userDetailsService.loadUserByUsername(user.getUsername())
        );
        this.tokenManagementService.addToken(token);
        return ResponseEntity.ok(token);
    }

    /**
     * Endpoint for logging out a user.
     *
     * @param request The request.
     * @param response The response.
     * @return A descriptive message of the logout.
     * @throws ServletException if a servlet error occurs.
     * @throws IOException if an I/O error occurs.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Logs out a user.")
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler()
                    .logout(request, response, authentication);
            this.logoutSuccessHandler
                    .onLogoutSuccess(request, response, authentication);
        }
        return ResponseEntity.ok("User logged out successfully.");
    }

    /**
     * Endpoint for fetching a user by its id.
     *
     * @param id The id of the user.
     * @return The user object.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Fetches a user using it's id.")
    @GetMapping("/users/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable final Long id) {
        return ResponseEntity.ok(appUserService.getUserById(id));
    }
}
