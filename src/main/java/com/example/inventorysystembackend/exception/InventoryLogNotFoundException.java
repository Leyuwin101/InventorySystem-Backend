package com.example.inventorysystembackend.exception;

import com.example.inventorysystembackend.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class InventoryLogNotFoundException extends BaseException {
    public InventoryLogNotFoundException(String message) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.INVENTORY_LOG_NOT_FOUND,
                "Inventory log not found",
                message);
    }
}
