package com.example.inventorysystembackend.exception;

import com.example.inventorysystembackend.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class AuthException extends BaseException {
    public AuthException(String message) {
        super(
                HttpStatus.UNAUTHORIZED,
                ErrorCode.AUTH_FAILED,
                "Authentication failed",
                message);
    }
}
