package com.example.inventorysystembackend.dto.shared;

import com.example.inventorysystembackend.dto.shared.response.ApiRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ResponseFactory {

    public static <T>ResponseEntity<ApiRes<T>> success(String message, T data) {
        return ResponseEntity.ok(
                new ApiRes<>("success", message, data)
        );
    }

    public static <T> ResponseEntity<ApiRes<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRes<>("success", message, data));
    }
}
