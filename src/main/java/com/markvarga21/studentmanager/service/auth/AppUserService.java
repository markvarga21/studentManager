package com.markvarga21.studentmanager.service.auth;

import com.markvarga21.studentmanager.entity.AppUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The AppUserService interface.
 */
@Service
public interface AppUserService {
    /**
     * This method is used to get the user by the username.
     *
     * @param username The username of the user.
     * @return The AppUser object.
     */
    Optional<AppUser> getUserByUsername(String username);

    /**
     * This method is used to register a user.
     *
     * @param user The user object.
     * @return The AppUser object.
     */
    AppUser registerUser(AppUser user);

    /**
     * Method for fetching all users from the database.
     *
     * @return A list of all users.
     */
    List<AppUser> getAllUsers();

    /**
     * Method for deleting a user from the database.
     *
     * @param id The id of the user.
     * @return The username of the deleted user.
     */
    String deleteUser(Long id);

    /**
     * Method for fetching a user by the id.
     *
     * @param id the id of the user.
     * @return The AppUser object.
     */
    AppUser getUserById(Long id);
}
