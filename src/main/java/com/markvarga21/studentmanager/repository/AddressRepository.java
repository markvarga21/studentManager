package com.markvarga21.studentmanager.repository;

import com.markvarga21.studentmanager.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository holding the students addresses.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
