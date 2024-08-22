package com.markvarga21.studentmanager.service.auth;

import com.markvarga21.studentmanager.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The {@code AppUserService} interface containing
 * methods for handling user related operations.
 */
@Service
public interface AppUserService {
    /**
     * This method is used to get the user by its username.
     *
     * @param username The username of the user.
     * @return The {@code AppUser} object.
     */
    Optional<AppUser> getUserByUsername(String username);

    /**
     * This method is used to register a user.
     *
     * @param user The user object.
     * @return The {@code AppUser} object.
     */
    AppUser registerUser(AppUser user);

    /**
     * This method is used to fetch all users.
     *
     * @param page The page number.
     * @param size The number of users in a single page.
     * @return A page of users.
     */
    Page<AppUser> getAllUsers(Integer page, Integer size);

    /**
     * Method for deleting a user from the database.
     *
     * @param id The id of the user.
     * @return The username of the deleted user.
     */
    String deleteUserById(Long id);

    /**
     * Method for deleting a user from the database by its username.
     *
     * @param username The username of the user.
     * @return An informative message.
     */
    String deleteUserByUsername(String username);

    /**
     * Method for fetching a user by its id.
     *
     * @param id the id of the user.
     * @return The {@code AppUser} object.
     */
    AppUser getUserById(Long id);


    /**
     * Method for granting roles to a user.
     *
     * @param username The username of the user.
     * @param roles The roles to be granted separated by commas.
     * @return A status message.
     */
    String grantRoles(String username, String roles);

    /**
     * Method for revoking roles from a user.
     *
     * @param username The username of the user.
     * @param roles The roles to be revoked separated by commas.
     * @return A status message.
     */
    String revokeRoles(String username, String roles);
}
