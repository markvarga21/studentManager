package com.markvarga21.studentmanager.config.security;

import com.markvarga21.studentmanager.service.auth.TokenManagementService;
import com.markvarga21.studentmanager.service.auth.webtoken.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This class is responsible for filtering the requests and responses
 * while checking the user's permissions/roles.
 */
@Configuration
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    /**
     * The service for managing JWT tokens.
     */
    private final JwtService jwtService;

    /**
     * The service for managing user details.
     */
    private final UserDetailsService userDetailsService;

    /**
     * The service for managing tokens.
     */
    private final TokenManagementService tokenManagementService;

    /**
     * The index where the token starts in the Authorization
     * header value.
     */
    static final int TOKEN_START_INDEX = 7;

    /**
     * Method for internal filtering.
     *
     * @param request The request object.
     * @param response The response object.
     * @param filterChain The filter chain object.
     * @throws ServletException The exception thrown if the servlet encounters a problem.
     * @throws IOException The exception thrown if an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.substring(TOKEN_START_INDEX);
        if (this.tokenManagementService.isBlacklisted(token)) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token has been blacklisted."
            );
            return;
        }
        String username = this.jwtService.getUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService
                    .loadUserByUsername(username);
            if (userDetails != null && jwtService.isValidToken(token)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                userDetails.getPassword(),
                                userDetails.getAuthorities()
                        );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        }
    }
}
