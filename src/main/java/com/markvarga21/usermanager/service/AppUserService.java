package com.markvarga21.usermanager.service;

import com.markvarga21.usermanager.dto.AppUserDto;

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
     *
     * @param appUserDto the user which you want to save in the application.
     * @return the recently created user.
     */
    AppUserDto createUser(AppUserDto appUserDto);

    /**
     * Retrieves a user from the application using its id.
     *
     * @param id the identifier of the user we want to retrieve.
     * @return the searched user.
     */
    AppUserDto getUserById(Long id);

    /**
     * Modifies the user's information.
     *
     * @param appUserDto the modified user information.
     * @param id the identifier of the user you want to modify.
     * @return the newly modified user's dto.
     */
    AppUserDto modifyUserById(AppUserDto appUserDto, Long id);

    /**
     * Deletes a user by its id.
     *
     * @param id the identifier used for deleting a user.
     * @return the recently deleted user's dto.
     */
    AppUserDto deleteUserById(Long id);
}
