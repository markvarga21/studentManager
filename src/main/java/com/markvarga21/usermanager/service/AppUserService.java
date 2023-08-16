package com.markvarga21.usermanager.service;

import com.markvarga21.usermanager.dto.AppUserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * An interface which contains essential methods for manipulating the user information.
 */
public interface AppUserService {
    /**
     * A method used for retrieving all the users in the application.
     *
     * @return all to users stored in a {@code List}.
     */
    List<AppUserDto> getAllUsers();

    /**
     * Saves a user in the application.
     * //TODO
     */
    AppUserDto createUser(MultipartFile idDocument, MultipartFile selfiePhoto, String appUserJson, String identification);

    /**
     * Retrieves a user from the application using its id.
     *
     * @param id the identifier of the user we want to retrieve.
     * @return the searched user.
     */
    AppUserDto getUserById(Long id);

    /**
     * Modifies the user's information.
     * //TODO
     */
    AppUserDto modifyUserById(MultipartFile idDocument, MultipartFile selfiePhoto, String appUserJson, Long userId, String identification);

    /**
     * Deletes a user by its id.
     *
     * @param id the identifier used for deleting a user.
     * @return the recently deleted user's dto.
     */
    AppUserDto deleteUserById(Long id);
}
