package com.example.inventorysystembackend.exception;

import com.example.inventorysystembackend.model.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCode errorCode;
    private final String title;

    protected BaseException(
            HttpStatus status,
            ErrorCode errorCode,
            String title,
            String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
        this.title = title;
    }

}
