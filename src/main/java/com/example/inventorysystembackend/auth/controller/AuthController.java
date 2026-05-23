package com.example.inventorysystembackend.auth.controller;

import com.example.inventorysystembackend.auth.dto.request.AuthRequest;
import com.example.inventorysystembackend.auth.dto.request.RefreshRequest;
import com.example.inventorysystembackend.auth.dto.response.AuthResponse;
import com.example.inventorysystembackend.auth.dto.response.AuthUserResponse;
import com.example.inventorysystembackend.auth.service.AuthService;
import com.example.inventorysystembackend.dto.request.UpdateAccountRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Tag(name = "Authentication", description = "Login and Token APIs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Value("${app.refresh-cookie.name:refreshToken}")
    private String refreshCookieName;

    @Value("${app.refresh-cookie.max-age-seconds:604800}")
    private long refreshCookieMaxAgeSeconds;

    @Value("${app.refresh-cookie.secure:true}")
    private boolean refreshCookieSecure;

    @Operation(summary = "Login user and return access + refresh tokens")
    @ApiResponse(responseCode = "200", description = "Login Successful")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, buildRefreshCookie(response.getRefreshToken()).toString())
                .body(response);
    }

    @Operation(summary = "Refresh access token using refresh token")
    @ApiResponse(responseCode = "200", description = "Token refreshed")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody(required = false) RefreshRequest request,
            HttpServletRequest httpRequest
    ) {
        String requestToken = request == null ? null : request.getRefreshToken();
        String refreshToken = hasText(requestToken) ? requestToken : readRefreshCookie(httpRequest);

        AuthResponse response = authService.refreshAccessToken(refreshToken);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, buildRefreshCookie(response.getRefreshToken()).toString())
                .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthUserResponse> getCurrentUser() {
        AuthUserResponse user = authService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<AuthUserResponse> updateCurrentUser(@Valid @RequestBody UpdateAccountRequest request) {
        AuthUserResponse user = authService.updateCurrentUser(request);
        return ResponseEntity.ok(user);
    }

    private ResponseCookie buildRefreshCookie(String refreshToken) {
        return ResponseCookie.from(refreshCookieName, refreshToken)
                .httpOnly(true)
                .secure(refreshCookieSecure)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofSeconds(refreshCookieMaxAgeSeconds))
                .build();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String readRefreshCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (refreshCookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
