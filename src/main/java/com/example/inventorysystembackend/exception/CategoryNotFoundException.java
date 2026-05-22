package com.example.inventorysystembackend.exception;

import com.example.inventorysystembackend.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends BaseException {
    public CategoryNotFoundException(String message) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.CATEGORY_NOT_FOUND,
                "CategoryNotFound",
                message);
    }
}
