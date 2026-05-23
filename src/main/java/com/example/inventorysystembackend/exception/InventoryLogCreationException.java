package com.example.inventorysystembackend.exception;

import com.example.inventorysystembackend.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class InventoryLogCreationException extends BaseException {

    public InventoryLogCreationException(String message) {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVENTORY_LOG_CREATION_FAILED,
                "Inventory Log Creation Failed",
                message
        );
    }
}
