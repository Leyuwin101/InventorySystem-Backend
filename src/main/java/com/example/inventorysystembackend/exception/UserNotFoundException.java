package com.example.inventorysystembackend.exception;

import com.example.inventorysystembackend.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.USER_NOT_FOUND,
                "User not found",
                message);
    }
}
