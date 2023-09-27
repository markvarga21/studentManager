package com.markvarga21.usermanager.controller;

import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.service.AppUserService;
import com.markvarga21.usermanager.service.faceapi.FaceApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * A controller which is used to make create, read, update and delete users.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin
public class AppUserController {
    /**
     * App user service.
     */
    private final AppUserService userService;

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
     * @return the saved {@code AppUserDto}.
     */
    @PostMapping
    public ResponseEntity<AppUserDto> createUser(
            @RequestParam("idDocument") final MultipartFile idDocument,
            @RequestParam("selfiePhoto") final MultipartFile selfiePhoto,
            @RequestParam("appUserJson") final String appUserJson
    ) {

        AppUserDto createdUser = this.userService.createUser(
                idDocument,
                selfiePhoto,
                appUserJson
        );
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Retrieves the desired user using its ID, then returns it.
     *
     * @param id the ID of the user which we want to retrieve.
     * @return the searched user if present.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getUserById(@PathVariable final Long id) {
        AppUserDto foundUser = this.userService.getUserById(id);
        return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }

    /**
     * Updates a user and then retrieves it.
     *
     * @param idDocument a photo of the users ID card or passport.
     * @param selfiePhoto a selfie photo for verifying identity.
     * @param appUserJson the user itself in a JSON string.
     * @param userId the ID of the user which has to be updated.
     * @return the updated {@code AppUserDto}.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<AppUserDto> updateUserById(
            @RequestParam("idDocument") final MultipartFile idDocument,
            @RequestParam("selfiePhoto") final MultipartFile selfiePhoto,
            @RequestParam("appUserJson") final String appUserJson,
            @PathVariable("userId") final Long userId
            ) {
        AppUserDto updatedUser = this.userService.modifyUserById(
                idDocument,
                selfiePhoto,
                appUserJson,
                userId
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
    public ResponseEntity<AppUserDto> deleteUserById(
            @PathVariable final Long id
    ) {
        AppUserDto deletedUser = this.userService.deleteUserById(id);
        return new ResponseEntity<>(deletedUser, HttpStatus.OK);
    }

    /**
     * Extracts and returns the data from the passport.
     *
     * @param passport the photo of the passport.
     * @return the extracted {@code AppUserDto} object.
     */
    @PostMapping("/extractDataFromPassport")
    public ResponseEntity<AppUserDto> getDataFromPassport(
            @RequestParam("passport") final MultipartFile passport
    ) {
        AppUserDto appUserDto = this.userService
                .extractDataFromPassport(passport);
        return new ResponseEntity<>(appUserDto, HttpStatus.OK);
    }
}
