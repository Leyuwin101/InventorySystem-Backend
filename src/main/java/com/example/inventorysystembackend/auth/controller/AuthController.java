package com.example.inventorysystembackend.auth.controller;

import com.example.inventorysystembackend.auth.dto.request.AuthRequest;
import com.example.inventorysystembackend.auth.dto.request.RefreshRequest;
import com.example.inventorysystembackend.auth.dto.response.AuthResponse;
import com.example.inventorysystembackend.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Login and Token APIs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Login user and return access + refresh tokens")
    @ApiResponse(responseCode = "200", description = "Login Successful")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Refresh access token using refresh token")
    @ApiResponse(responseCode = "200", description = "Token refreshed")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {

        AuthResponse response = authService.refreshAccessToken(request.getRefreshToken());

        return ResponseEntity.ok(response);
    }
}
