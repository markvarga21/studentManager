package com.markvarga21.usermanager.controller;

import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.service.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A controller which is used to make create, read, update and delete users.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class AppUserController {
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
     * Saves a user in the database and then returns it.
     *
     * @param appUserDto the user which has been stored.
     * @return If used correctly, it sends back the saved user.
     */
    @PostMapping
    public ResponseEntity<AppUserDto> createUser(@RequestBody @Valid AppUserDto appUserDto) {
        AppUserDto createdUser = this.userService.createUser(appUserDto);
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
     * @param appUserDto the user which has been modified.
     * @param id the ID of the user which we want to modify.
     * @return the newly updated user DTO object.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppUserDto> updateUserById(@RequestBody AppUserDto appUserDto, @PathVariable Long id) {
        AppUserDto updatedUser = this.userService.modifyUserById(appUserDto, id);
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
