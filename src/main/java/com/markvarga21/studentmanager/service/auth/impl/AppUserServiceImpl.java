package com.markvarga21.studentmanager.service.auth.impl;

import com.markvarga21.studentmanager.dto.Role;
import com.markvarga21.studentmanager.entity.AppUser;
import com.markvarga21.studentmanager.exception.InvalidUserCredentialsException;
import com.markvarga21.studentmanager.exception.UserNotFoundException;
import com.markvarga21.studentmanager.repository.AppUserRepository;
import com.markvarga21.studentmanager.service.auth.AppUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The AppUserServiceImpl class is used to implement the AppUserService interface.
 */
@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class AppUserServiceImpl implements AppUserService {
    /**
     * The AppUserRepository object.
     */
    private final AppUserRepository repository;

    /**
     * This method is used to get the user by the username.
     *
     * @param username The username of the user.
     * @return The AppUser object.
     */
    @Override
    public Optional<AppUser> getUserByUsername(final String username) {
        return this.repository.findByUsername(username);
    }

    /**
     * This method is used to register a user.
     *
     * @param user The user object.
     * @return The AppUser object.
     */
    @Override
    public AppUser registerUser(final AppUser user) {
        log.info("Registering user: {}", user);
        Optional<AppUser> userOptional = this.repository
                .findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            String message = "User with username " + user.getUsername() + " already exists.";
            log.error(message);
            throw new InvalidUserCredentialsException(message);
        }

        Set<Role> userRole = new HashSet<>();
        userRole.add(Role.USER);
        user.setRoles(userRole);
        return this.repository.save(user);
    }

    /**
     * Method for fetching all users from the database.
     *
     * @return A list of all users.
     */
    @Override
    public List<AppUser> getAllUsers() {
        return this.repository.findAll();
    }

    /**
     * Method for deleting a user from the database.
     *
     * @param id The id of the user.
     * @return The username of the deleted user.
     */
    @Override
    public String deleteUserById(final Long id) {
        Optional<AppUser> userOptional = this.repository.findById(id);
        if (userOptional.isEmpty()) {
            String message = "User with id " + id + " not found.";
            log.error(message);
            throw new UserNotFoundException(message);
        }
        if (userOptional.get().getRoles().contains(Role.ADMIN)) {
            String message = "Cannot delete an admin user.";
            log.error(message);
            throw new InvalidUserCredentialsException(message);
        }
        this.repository.deleteById(id);
        return String.format(
                "User with username '%s' has been deleted.",
                userOptional.get().getUsername()
        );
    }

    /**
     * Method for fetching a user by the id.
     *
     * @param id the id of the user.
     * @return The AppUser object.
     */
    @Override
    public AppUser getUserById(final Long id) {
        Optional<AppUser> userOptional = this.repository.findById(id);
        if (userOptional.isEmpty()) {
            String message = "User with id " + id + " not found.";
            log.error(message);
            throw new UserNotFoundException(message);
        }
        return userOptional.get();
    }
}
