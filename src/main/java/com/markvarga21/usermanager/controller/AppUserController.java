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

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class AppUserController {
    private final AppUserService userService;

    @GetMapping
    public List<AppUserDto> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<AppUserDto> createUser(@RequestBody @Valid AppUserDto appUserDto) {
        AppUserDto createdUser = this.userService.createUser(appUserDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getUserById(@PathVariable Long id) {
        AppUserDto foundUser = this.userService.getUserById(id);
        return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserDto> updateUserById(@RequestBody AppUserDto appUserDto, @PathVariable Long id) {
        AppUserDto updatedUser = this.userService.modifyUserById(appUserDto, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppUserDto> deleteUserById(@PathVariable Long id) {
        AppUserDto deletedUser = this.userService.deleteUserById(id);
        return new ResponseEntity<>(deletedUser, HttpStatus.OK);
    }
}
