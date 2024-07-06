package com.markvarga21.studentmanager.service.auth.security;

import com.markvarga21.studentmanager.entity.AppUser;
import com.markvarga21.studentmanager.service.auth.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The AppUserDetailsService class is used to implement the UserDetailsService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserDetailsService implements UserDetailsService {
    /**
     * The AppUserRepository object.
     */
    private final AppUserService appUserService;

    /**
     * This method is used to load the user by the username.
     *
     * @param username The username of the user.
     * @return The UserDetails object.
     * @throws UsernameNotFoundException If the username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        Optional<AppUser> userOptional = this.appUserService
                .getUserByUsername(username);
        if (userOptional.isEmpty()) {
            String message = "User not found with username: " + username;
            log.error(message);
            throw new UsernameNotFoundException(message);
        }

        String[] roleArray = userOptional
                .get()
                .getRoles()
                .stream()
                .map(Enum::name)
                .toArray(String[]::new);
        return User.builder()
                .username(userOptional.get().getUsername())
                .password(userOptional.get().getPassword())
                .roles(roleArray)
                .build();
    }
}
