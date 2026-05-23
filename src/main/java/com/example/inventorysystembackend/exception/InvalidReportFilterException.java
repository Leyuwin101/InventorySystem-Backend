package com.example.inventorysystembackend.exception;

import com.example.inventorysystembackend.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidReportFilterException extends BaseException {
    public InvalidReportFilterException(String message) {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_REPORT_FILTER,
                "Invalid Report Filter",
                message
        );
    }
}
