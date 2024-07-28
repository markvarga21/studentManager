package com.markvarga21.studentmanager.service.auth.webtoken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A service class for handling JWT tokens.
 */
@Service
@Setter
public final class JwtService {
    private JwtService() {
    }

    /**
     * The secret key used for signing the JWT token.
     */
    @Value("${jwt.secret.key}")
    private String secretKey;

    /**
     * The expiration time for the JWT token
     * measured in milliseconds.
     */
    @Value("${jwt.expiration.time.minutes}")
    private Long expirationTimeInMinutes;

    /**
     * The issuer of the JWT token.
     */
    @Value("${jwt.token.issuer}")
    private String jwtTokenIssuer;

    /**
     * The audience of the JWT token.
     */
    @Value("${jwt.token.audience}")
    private String jwtTokenAudience;

    /**
     * A method for generating a JWT token.
     *
     * @param userDetails The user details.
     * @return The generated JWT token.
     */
    public String generateJwtToken(final UserDetails userDetails) {
        Map<String, String> claims = new HashMap<>();
        claims.put("iss", this.jwtTokenIssuer);
        claims.put("aud", this.jwtTokenAudience);
        claims.put("sub", userDetails.getUsername());
        claims.put("iat", String.valueOf(
                TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis()))
        );
        claims.put("exp", String.valueOf(
                TimeUnit.MILLISECONDS
                        .toMillis(this.getExpirationTime().getTime()))
        );
        claims.put("roles", userDetails.getAuthorities().toString());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(this.getExpirationTime())
                .signWith(this.generateSecretKey())
                .compact();
    }

    /**
     * A method for generating the secret key object.
     *
     * @return The secret key object.
     */
    private SecretKey generateSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(this.secretKey);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    /**
     * Converts the millisecond expiration time to a {@link Date} object.
     *
     * @return The expiration time as a {@link Date} object.
     */
    private Date getExpirationTime() {
        return Date.from(Instant
                .now()
                .plusMillis(TimeUnit.MINUTES.toMillis(this.expirationTimeInMinutes))
        );
    }

    /**
     * A method for retrieving the username from a JWT token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    public String getUsername(final String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    private Claims extractClaims(final String token) {
        return Jwts.parser()
                .verifyWith(this.generateSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * A method for checking if a JWT token is valid or not.
     *
     * @param token The JWT token.
     * @return A boolean indicating if the token is valid or not.
     */
    public boolean isValidToken(final String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }
}
