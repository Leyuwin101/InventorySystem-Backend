package com.example.inventorysystembackend.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET must be set and at least 32 characters long");
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate token
     *
     * The token contains:
     * - The user's email as the subject
     * - The user's id, username, and role as separate custom claims
     * - Issued and expiration timestamps
     *
     * Token validity: 1 hour
     *
     * @param email authenticated user's email
     * @param userId authenticated user's id
     * @param username authenticated user's username
     * @param role authenticated user's role
     * @return signed JWT access token
     */
    public String generateToken(String email, Long userId, String username, String role) {

        // Create a JWT Token
        return Jwts.builder()
                .setSubject(email) // store the email inside the token(payload)
                .claim("userId", userId)
                .claim("username", username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Expiration time 1 hour
                .signWith(key)
                .compact(); // Convert everything into a compact JWT string
    }

    /**
     * Extracts the email (subject) from a JWT token.
     *
     * The token is validated using the application's signing key
     * before the subject is retrieved.
     *
     * @param token JWT token
     * @return email stored in the token
     */
    public String extractEmail(String token) {

        if (token == null || token.isBlank()) {
            throw new JwtException("JWT token is missing");
        }

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Generates a JWT refresh token.
     *
     * Refresh tokens are used to issue new access tokens
     * without requiring the user to log in again.
     *
     * Token validity: 7 days
     *
     * @param username authenticated user's username
     * @return signed JWT refresh token
     */
    public String generateRefreshToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7 DAYS
                .signWith(key)
                .compact();
    }


    /**
     * Validates a JWT token against the provided email
     * and checks whether the token is still active.
     *
     * @param token JWT token
     * @param email expected user email
     * @return true if token is valid and not expired
     */
    public boolean validateToken(String token, String email) {

        if (email == null || email.isBlank()) {
            return false;
        }

        String extractEmail = extractEmail(token);

        return extractEmail.equals(email) && !isTokenExpired(token);
    }

    /**
     * Checks whether the JWT token has expired.
     *
     * @param token JWT token
     * @return true if token expiration date is before current time
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration timestamp from a JWT token.
     *
     * @param token JWT token
     * @return token expiration date
     */
    private Date extractExpiration(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    /**
     * Extracts the user role stored in JWT claims.
     *
     * @param token JWT token
     * @return user role
     */
    private String extractRole(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}


