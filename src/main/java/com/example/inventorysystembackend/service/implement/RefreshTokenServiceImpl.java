package com.example.inventorysystembackend.service.implement;

import com.example.inventorysystembackend.exception.RefreshTokenException;
import com.example.inventorysystembackend.model.entity.RefreshToken;
import com.example.inventorysystembackend.model.entity.User;
import com.example.inventorysystembackend.repository.RefreshTokenRepository;
import com.example.inventorysystembackend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.UUID;

@Validated
@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Creates and stores a new refresh token for the given user.
     *
     * Each token is:
     * - uniquely generated (UUID)
     * - tied to a specific user and device
     * - valid for 7 days
     * - initially not revoked
     *
     * This is used to issue new access token without re-authenticating
     *
     * @param user authenticated user
     * @param deviceInfo client device info
     * @return persisted refresh token entity
     */
    @Override
    public RefreshToken createRefreshToken(User user, String deviceInfo) {

        log.info("[REFRESH-TOKEN][CREATE] Creating refresh token for userId={}, device={}", user.getUserID(), deviceInfo);

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusDays(7));
        token.setDeviceInfo(deviceInfo);
        token.setRevoked(false);

        RefreshToken saved = refreshTokenRepository.save(token);

        log.info("[REFRESH-TOKEN][CREATE] refresh token saved for userId={}, device={}", user.getUserID(), deviceInfo);

        return saved;
    }

    /**
     * Validates a refresh token and returns the stored token entity if valid.
     *
     * Validation rules:
     * - token must exist in the database
     * - token must not be expired
     * - token must not be revoked
     *
     * @param token refresh token string
     * @return valid RefreshToken entity
     * @throws RefreshTokenException if token is invalid, expired, or revoked
     */
    @Override
    public RefreshToken validate(String token) {

        log.info("[REFRESH-TOKEN][VALIDATE] validating refresh token");

        if (token == null || token.isBlank()) {
            log.warn("[REFRESH-TOKEN][VALIDATE] Missing refresh token");
            throw new RefreshTokenException("Refresh token missing");
        }

        RefreshToken storedToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("[REFRESH-TOKEN][VALIDATE] Invalid refresh token");
                    return new RefreshTokenException("Invalid refresh token");
                });

        if(storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            Long userId = storedToken.getUser() == null ? null : storedToken.getUser().getUserID();
            log.warn("[REFRESH-TOKEN][VALIDATE] Expired refresh token used for userId={}", userId);
            throw new RefreshTokenException("Refresh token is expired");
        }

        if(storedToken.isRevoked()) {
            log.warn("[REFRESH-TOKEN][VALIDATE] Revoked refresh token attempt tokenPrefix={}", token.substring(0, Math.min(10, token.length())));
            throw new RefreshTokenException("Refresh token is revoked");
        }

        log.info("[REFRESH-TOKEN][VALIDATE] Refresh token validated for userId={}", storedToken.getUser().getUserID());

        return storedToken;

    }

    /**
     * Revokes a refresh token, making it invalid for future use.
     *
     * Once revoked, the token can no longer be used to generate new access tokens,
     * even if it has not yet expired.
     *
     * @param token refresh token entity to revoke
     */
    @Override
    public void revokeToken(RefreshToken token) {
        log.info("[REFRESH-TOKEN][REVOKE] Revoking refresh token for userId={}", token.getUser().getUserID());

        token.setRevoked(true);
        refreshTokenRepository.save(token);

        log.info("[REFRESH-TOKEN][REVOKE] Refresh token revoked for userId={}", token.getUser().getUserID());
    }

}
