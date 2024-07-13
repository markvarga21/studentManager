package com.markvarga21.studentmanager.entity;

import com.markvarga21.studentmanager.dto.Role;
import com.markvarga21.studentmanager.util.Generated;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * The AppUser class is used to represent the user entity.
 */
@Entity
@Data
@Generated
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    /**
     * The id of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The username of the user.
     */
    @NotBlank(message = "Username cannot be null or empty!")
    private String username;

    /**
     * The email of the user.
     */
    @Email(message = "Email should be valid!")
    private String email;

    /**
     * The first name of the user.
     */
    @NotBlank(message = "First name cannot be null or empty!")
    private String firstName;

    /**
     * The last name of the user.
     */
    @NotBlank(message = "Last name cannot be null or empty!")
    private String lastName;

    /**
     * The password of the user.
     */
    @NotBlank(message = "Password cannot be null or empty!")
    private String password;

    /**
     * The roles of the user.
     */
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles;
}
