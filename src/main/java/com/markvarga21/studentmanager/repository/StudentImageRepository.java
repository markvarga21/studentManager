package com.markvarga21.studentmanager.repository;

import com.markvarga21.studentmanager.entity.StudentImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A repository which is used to store the student images.
 */
@Repository
public interface StudentImageRepository
        extends JpaRepository<StudentImage, String> {
    /**
     * Deletes images using the passport number.
     *
     * @param passportNumber The passport number.
     */
    void deleteStudentImagesByPassportNumber(
            String passportNumber
    );
}