package com.markvarga21.usermanager.repository;

import com.markvarga21.usermanager.entity.FacialValidationData;
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
     * Method signature for verifying if user is present
     * in the database.
     *
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     * @return the facial validation data.
     */
    Optional<FacialValidationData> findFacialValidationDataByFirstNameAndLastName(
            String firstName,
            String lastName
    );
}
