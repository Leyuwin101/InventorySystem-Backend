package com.example.inventorysystembackend.exception;

import com.example.inventorysystembackend.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class RefreshTokenException extends BaseException {
    public RefreshTokenException(String message) {
        super(
                HttpStatus.UNAUTHORIZED,
                ErrorCode.REFRESH_TOKEN_INVALID,
                "Invalid refresh token",
                message);
    }
}
