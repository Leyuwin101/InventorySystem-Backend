package com.example.inventorysystembackend.exception;

import com.example.inventorysystembackend.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class ExportFailedException extends BaseException {
    public ExportFailedException(String message) {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.EXPORT_FAILED,
                "Export Failed",
                message
        );
    }
}
