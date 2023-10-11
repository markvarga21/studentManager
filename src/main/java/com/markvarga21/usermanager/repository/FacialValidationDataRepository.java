package com.markvarga21.usermanager.repository;

import com.markvarga21.usermanager.dto.FacialValidationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A repository which is used to store the facial validation data.
 */
@Repository
public interface FacialValidationDataRepository
        extends JpaRepository<FacialValidationData, Long> {
    /**
     * Method signature for verifying if student is present
     * in the database.
     *
     * @param firstName The first name of the student.
     * @param lastName The last name of the student.
     * @return The facial validation data.
     */
    Optional<FacialValidationData> findFacialValidationDataByFirstNameAndLastName(
            String firstName,
            String lastName
    );
}
