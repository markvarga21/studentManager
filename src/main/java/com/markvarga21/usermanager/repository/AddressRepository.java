package com.markvarga21.usermanager.repository;

import com.markvarga21.usermanager.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository holding the users addresses.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
