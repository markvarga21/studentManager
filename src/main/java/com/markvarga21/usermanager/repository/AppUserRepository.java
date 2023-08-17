package com.markvarga21.usermanager.repository;

import com.markvarga21.usermanager.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository holding the applications users.
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
}
