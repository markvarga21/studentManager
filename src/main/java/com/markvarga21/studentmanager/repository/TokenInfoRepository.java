package com.markvarga21.studentmanager.repository;

import com.markvarga21.studentmanager.entity.TokenInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for the token info.
 */
@Repository
public interface TokenInfoRepository extends JpaRepository<TokenInfo, Long> {
    /**
     * Find a token by the token string.
     *
     * @param token the token string to search for.
     * @return the token info if found, empty otherwise.
     */
    Optional<TokenInfo> getTokenInfoByToken(String token);
}
