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
     * Validates and then persists a user in the database.
     *
     * @param idDocument a photo of the ID document or passport.
     * @param selfiePhoto a selfie file of the user.
     * @param appUserJson the user itself with the new data.
     * @param identification the identification type ('idDocument' or 'passport').
     * @return the newly modified user.
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
     * Modifies the user identified by its id.
     *
     * @param idDocument a photo of the ID document or passport.
     * @param selfiePhoto a selfie file of the user.
     * @param appUserJson the user itself with the new data.
     * @param userId the id of the user we want to modify.
     * @param identification the identification type ('idDocument' or 'passport').
     * @return the newly modified user.
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
