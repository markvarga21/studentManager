package com.markvarga21.usermanager.controller;

import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.service.AppUserService;
import com.markvarga21.usermanager.service.faceapi.FaceApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * A controller which is used to make create, read, update and delete users.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class AppUserController {
    /**
     * App user service.
     */
    private final AppUserService userService;
    private final FaceApiService faceApiService;

    /**
     * Retrieves all users which are present in the application.
     *
     * @return a {@code List} containing all the users.
     */
    @GetMapping
    public List<AppUserDto> getAllUsers() {
        return this.userService.getAllUsers();
    }

    /**
     * Saves and validates a user in the database and then returns it.
     *
     * @param idDocument a photo of the users ID card or passport.
     * @param selfiePhoto a selfie photo for verifying identity.
     * @param appUserJson the user itself in a JSON string.
     * @param identification the ID type ('passport' or 'idDocument').
     * @return the saved {@code AppUserDto}.
     */
    @PostMapping
    public ResponseEntity<AppUserDto> createUser(
            @RequestParam("idDocument") MultipartFile idDocument,
            @RequestParam("selfiePhoto") MultipartFile selfiePhoto,
            @RequestParam("appUserJson") String appUserJson,
            @RequestParam("identification") String identification
    ) {

        AppUserDto createdUser = this.userService.createUser(
                idDocument,
                selfiePhoto,
                appUserJson,
                identification
        );
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Retrieves the desired user using its ID, then returns it.
     *
     * @param id the ID of the user which we want to retrieve.
     * @return the searched used if present.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getUserById(@PathVariable Long id) {
        AppUserDto foundUser = this.userService.getUserById(id);
        return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }

    /**
     * Updates a user and then retrieves it.
     *
     * @param idDocument a photo of the users ID card or passport.
     * @param selfiePhoto a selfie photo for verifying identity.
     * @param appUserJson the user itself in a JSON string.
     * @param identification the ID type ('passport' or 'idDocument').
     * @param userId the ID of the user which has to be updated.
     * @return the updated {@code AppUserDto}.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<AppUserDto> updateUserById(
            @RequestParam("idDocument") MultipartFile idDocument,
            @RequestParam("selfiePhoto") MultipartFile selfiePhoto,
            @RequestParam("appUserJson") String appUserJson,
            @RequestParam("identification") String identification,
            @PathVariable("userId") Long userId
            ) {
        AppUserDto updatedUser = this.userService.modifyUserById(
                idDocument,
                selfiePhoto,
                appUserJson,
                userId,
                identification
        );
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Deletes a user and then retrieves it.
     *
     * @param id the ID of the user which we want to delete.
     * @return the recently deleted user DTO object.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<AppUserDto> deleteUserById(@PathVariable Long id) {
        AppUserDto deletedUser = this.userService.deleteUserById(id);
        return new ResponseEntity<>(deletedUser, HttpStatus.OK);
    }
}
