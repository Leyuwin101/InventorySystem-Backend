package com.example.inventorysystembackend.exception;

import com.example.inventorysystembackend.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidDateRange extends BaseException {
    public InvalidDateRange(String message) {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_DATE_RANGE,
                "Invalid Date Range",
                message
        );
    }
}
