package com.example.inventorysystembackend.auth.service;

import com.example.inventorysystembackend.auth.dto.request.AuthRequest;
import com.example.inventorysystembackend.auth.dto.response.AuthResponse;
import com.example.inventorysystembackend.auth.dto.response.AuthUserResponse;
import com.example.inventorysystembackend.auth.jwt.JwtUtil;
import com.example.inventorysystembackend.exception.AuthException;
import com.example.inventorysystembackend.exception.UserNotFoundException;
import com.example.inventorysystembackend.model.entity.RefreshToken;
import com.example.inventorysystembackend.model.entity.User;
import com.example.inventorysystembackend.repository.RefreshTokenRepository;
import com.example.inventorysystembackend.repository.UserRepository;
import com.example.inventorysystembackend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

        try {
            log.info("[AUTH] Authenticating user={}", request.getEmail());

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new AuthException("Invalid credentials"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new AuthException("Invalid credentials");
            }

            if (user.getRole() == null) {
                throw new AuthException("User role not configured");
            }

            String accessToken = util.generateToken(
                    user.getEmail(),
                    user.getRole().name()
            );

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user, "WEB");

            AuthResponse response = new AuthResponse();
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken.getToken());

            return response;

        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            log.error("[AUTH][LOGIN][FATAL]", e);
            throw new AuthException("Login failed due to server error");
        }
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

        log.info("[AUTH] Refreshing access token");

        try {

            if (refreshToken == null || refreshToken.isBlank()) {
                throw new AuthException("Refresh token missing");
            }

            RefreshToken storedToken = refreshTokenService.validate(refreshToken);

            if (storedToken == null) {
                throw new AuthException("Invalid refresh token");
            }

            User user = storedToken.getUser();

            if (user == null) {
                throw new AuthException("User not found");
            }

            log.info("[AUTH] Valid refresh token for userId={}", user.getUserID());

            /**
             * Revoke old token
             */
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);

            /**
             * Generate new access token
             */
            String newAccessToken = util.generateToken(
                    user.getEmail(),
                    user.getRole().name()
            );

            /**
             * Rotate refresh token
             */
            RefreshToken newRefreshToken =
                    refreshTokenService.createRefreshToken(user, "WEB");

            log.info("[AUTH] Token rotation completed for userId={}",
                    user.getUserID());

            AuthResponse response = new AuthResponse();

            response.setAccessToken(newAccessToken);
            response.setRefreshToken(newRefreshToken.getToken());

            return response;

        } catch (Exception e) {

            log.error("[AUTH] Refresh token failed", e);

            throw new AuthException("Refresh token expired or invalid");
        }
    }

    public AuthUserResponse getCurrentUser() {

        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || auth.getPrincipal().equals("anonymousUser")) {
            throw new AuthException("Not authenticated");
        }

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("User not found"));

        return new AuthUserResponse(
                user.getEmail(),
                user.getUsername(),
                user.getRole()
        );
    }

}
