package com.example.inventorysystembackend.auth.service;

import com.example.inventorysystembackend.auth.dto.request.AuthRequest;
import com.example.inventorysystembackend.auth.dto.response.AuthResponse;
import com.example.inventorysystembackend.auth.jwt.JwtUtil;
import com.example.inventorysystembackend.exception.AuthException;
import com.example.inventorysystembackend.model.entity.RefreshToken;
import com.example.inventorysystembackend.model.entity.User;
import com.example.inventorysystembackend.repository.RefreshTokenRepository;
import com.example.inventorysystembackend.repository.UserRepository;
import com.example.inventorysystembackend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil util;


    /**
     * Authenticates a user using email and password.
     *
     * On successful authentication:
     * - generates a JWT access token
     * - creates a refresh token for session persistence
     * - returns both tokens to the client
     *
     * @param request login credentials
     * @return authentication response containing access and refresh tokens
     * @throws AuthException if credentials are invalid
     */
    public AuthResponse login(AuthRequest request) {

        log.info("[AUTH] Authenticating user={}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("[AUTH] User not found with email={}", request.getEmail());
                    return new AuthException("Invalid credentials");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException("Invalid credentials");
        };

        String accessToken = util.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user, "WEB");

        log.info("[AUTH] Login successful for email:{}", user.getEmail());

        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken.getToken());

        return response;
    }

    /**
     * Generates a new access token using a valid refresh token.
     *
     * Security flow:
     * - validates the provided refresh token
     * - revokes the old refresh token
     * - issues a new access token
     * - creates a new refresh token (token rotation)
     *
     * Token rotation helps prevent replay attacks by ensuring
     * refresh tokens are single-use.
     *
     * @param refreshToken existing refresh token
     * @return new access and refresh token pair
     */
    public AuthResponse refreshAccessToken(String refreshToken) {

        log.info("[AUTH] Refreshing access tokens");

        RefreshToken storedToken = refreshTokenService.validate(refreshToken);

        User user = storedToken.getUser();

        log.info("[AUTH] Valid refresh token for userId={}", user.getUserID());

        // Revoke old refresh tokens
        storedToken.setRevoked(true);
        tokenRepository.save(storedToken);

        // Create new access token
        String newAccessToken = util.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        // Create new Refresh token (Rotation)
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user, "WEB");

        log.info("[AUTH] Token rotation completed for userId={}", user.getUserID() );

        AuthResponse response = new AuthResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken.getToken());

        return response;
    }

}
