package com.example.inventorysystembackend.exception;

import com.example.inventorysystembackend.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistException extends BaseException {
    public EmailAlreadyExistException(String message) {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.EMAIL_ALREADY_EXISTS,
                "Email Already exists",
                message
        );
    }
}
