package com.example.inventorysystembackend.exception;

import com.example.inventorysystembackend.dto.shared.response.ErrorRes;
import com.example.inventorysystembackend.model.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles request validation errors triggered by @Valid annotations.
     *
     * Extracts all field validation messages and returns
     * a standardized BAD_REQUEST response.
     *
     * Example:
     * - email: must not be blank
     * - password: size must be at least 8
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRes> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .reduce("", (a,b) -> a + b + "; ");

        log.warn("[VALIDATION][FAILED] Validation error: {}", details);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorRes(
                        ErrorCode.VALIDATION_ERROR,
                        "Validation error",
                        details,
                        request.getRequestURI(),
                        LocalDateTime.now()
                ));

    }


    /**
     * Handles all custom application exceptions that extend BaseException.
     *
     * This includes:
     * - resource not found exceptions
     * - authentication exceptions
     * - token validation exceptions
     * - business rule violations
     *
     * Each exception already contains:
     * - HTTP status
     * - error code
     * - response title
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorRes> handleBaseException(
            BaseException ex,
            HttpServletRequest request
    ) {

        log.warn(
                "[EXCEPTION][{}] {}",
                ex.getErrorCode(),
                ex.getMessage()
        );

        return buildErrorResponse(
                ex.getStatus(),
                ex.getErrorCode(),
                ex.getTitle(),
                ex.getMessage(),
                request
        );
    }

    /**
     * Fallback handler for unexpected application errors.
     *
     * Prevents internal exceptions from exposing raw stack traces
     * to API consumers.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRes> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {

        log.error("[EXCEPTION][ERROR] Unexpected error occurred", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorRes(
                        ErrorCode.INTERNAL_ERROR,
                        "Unexpected Error",
                        ex.getMessage(),
                        request.getRequestURI(),
                        LocalDateTime.now()
                ));
    }

    /**
     * Builds a standardized API error response.
     *
     * Centralizing error response creation ensures:
     * - consistent response format
     * - reduced duplicated code
     * - easier maintenance
     */
    private ResponseEntity<ErrorRes> buildErrorResponse(
            HttpStatus status,
            ErrorCode errorCode,
            String title,
            String details,
            HttpServletRequest request
    ) {

        ErrorRes error = new ErrorRes(
                errorCode,
                title,
                details,
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(status)
                .body(error);
    }

}
