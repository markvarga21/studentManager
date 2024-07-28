package com.markvarga21.studentmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * The entity for the student app user used, which manages the
 * relationship between the student's user profile and their
 * already submitted data.
 */
@Entity
@Data
public class StudentAppUser {
    /**
     * The ID of the student app user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The username of the student.
     */
    private String username;

    /**
     * The ID of the student's data.
     */
    private Long studentId;
}
