package com.example.inventorysystembackend.exception;

import com.example.inventorysystembackend.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class DuplicateSkuException extends BaseException {
    public DuplicateSkuException(String message) {

        super(
                HttpStatus.CONFLICT,
                ErrorCode.DUPLICATE_SKU,
                "Duplicate SKU",
                message
        );
    }
}
