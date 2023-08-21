package com.markvarga21.usermanager.repository;

import com.markvarga21.usermanager.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The repository holding the applications users.
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    /**
     * A method which is used to verify whether a user already exists
     * in the database with the same first name and last name or not.
     *
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     * @return an optional {@code AppUser} object.
     */
    Optional<AppUser> findAppUserByFirstNameAndLastName(
            String firstName,
            String lastName
    );
}
