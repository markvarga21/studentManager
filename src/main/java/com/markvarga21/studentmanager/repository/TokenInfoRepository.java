package com.markvarga21.studentmanager.repository;

import com.markvarga21.studentmanager.entity.TokenInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for storing token related information.
 */
@Repository
public interface TokenInfoRepository extends JpaRepository<TokenInfo, Long> {
    /**
     * Find a token by the token string.
     *
     * @param token The token string to search for.
     * @return The token info if found, empty otherwise.
     */
    Optional<TokenInfo> getTokenInfoByToken(String token);
}
