package com.fitzealot.userservice.service.custom;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {

    /**
     * Generates a JWT token for the given username.
     *
     * @param username the username to generate the token for
     * @return a signed JWT token
     */
    String generateToken(String username);

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    String extractUsername(String token);

    /**
     * Validates the JWT token against the provided user details.
     *
     * @param jwtToken the token to validate
     * @param userDetails the user details to compare with the token
     * @return true if valid, false otherwise
     */
    boolean validateToken(String jwtToken, UserDetails userDetails);
}
