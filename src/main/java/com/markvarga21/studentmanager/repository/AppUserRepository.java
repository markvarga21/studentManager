package com.markvarga21.studentmanager.repository;

import com.markvarga21.studentmanager.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The app user repository holding the users credentials.
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    /**
     * This method is used to find a user by username.
     *
     * @param username The username of the user.
     * @return The user object.
     */
    Optional<AppUser> findByUsername(String username);
}
