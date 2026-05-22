package com.example.inventorysystembackend.exception;

import com.example.inventorysystembackend.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class SaleNotFoundException extends BaseException {
    public SaleNotFoundException(String message) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.SALE_NOT_FOUND,
                "Sale not found",
                message);
    }
}
