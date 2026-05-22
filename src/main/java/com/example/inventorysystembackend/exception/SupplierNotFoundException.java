package com.example.inventorysystembackend.exception;

import com.example.inventorysystembackend.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class SupplierNotFoundException extends BaseException {
    public SupplierNotFoundException(String message) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.SUPPLIER_NOT_FOUND,
                "Supplier not found",
                message);
    }
}
