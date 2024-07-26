package com.markvarga21.studentmanager.repository;

import com.markvarga21.studentmanager.entity.StudentAppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The repository for the student application user.
 */
@Repository
public interface StudentAppUserRepository
        extends JpaRepository<StudentAppUser, Long> {
    /**
     * This method is used to delete a student app user by the student's ID.
     *
     * @param studentId The ID of the student.
     */
    void deleteByStudentId(Long studentId);

    /**
     * This method is used to find a student app user by the student's ID.
     *
     * @param username The username of the student.
     * @return The student app user.
     */
    Optional<StudentAppUser> findByUsername(String username);
}
