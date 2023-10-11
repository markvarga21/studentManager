package com.markvarga21.usermanager.service;

import com.markvarga21.usermanager.dto.StudentDto;

import java.util.List;

/**
 * An interface which contains essential methods
 * for manipulating user information.
 */
public interface AppUserService {
    /**
     * A method used for retrieving all the users in the application.
     *
     * @return all to users stored in a {@code List}.
     */
    List<StudentDto> getAllUsers();

    /**
     * Validates and then persists a user in the database.
     *
     * @param appUserJson the user itself with the new data.
     * @return the newly modified user.
     */
    StudentDto createUser(String appUserJson);

    /**
     * Retrieves a user from the application using its id.
     *
     * @param id the identifier of the user we want to retrieve.
     * @return the searched user.
     */
    StudentDto getUserById(Long id);

    /**
     * Modifies the user identified by its id.
     *
     * @param appUserJson the user itself with the new data.
     * @param userId the id of the user we want to modify.
     * @return the newly modified user.
     */
    StudentDto modifyUserById(String appUserJson, Long userId);

    /**
     * Deletes a user by its id.
     *
     * @param id the identifier used for deleting a user.
     * @return the recently deleted user's dto.
     */
    StudentDto deleteUserById(Long id);
}
