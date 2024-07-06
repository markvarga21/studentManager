package com.markvarga21.studentmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * A DTO utility class used for user login.
 */
@Data
public class UserLogin {
    /**
     * The username of the user.
     */
    @NotBlank(message = "Username is required.")
    private String username;

    /**
     * The password of the user.
     */
    @NotBlank(message = "Password is required.")
    private String password;
}
