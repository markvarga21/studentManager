package com.markvarga21.studentmanager.service.auth;

import com.markvarga21.studentmanager.entity.AppUser;
import org.springframework.data.domain.Page;
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
     * This method is used to get all the users.
     *
     * @param page The page number.
     * @param size The size of the page.
     * @return The list of users.
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
     * Method for fetching a user by the id.
     *
     * @param id the id of the user.
     * @return The AppUser object.
     */
    AppUser getUserById(Long id);
}
