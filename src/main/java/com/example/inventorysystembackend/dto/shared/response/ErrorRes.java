package com.example.inventorysystembackend.dto.shared.response;

import com.example.inventorysystembackend.model.enums.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "Error response wrapper")
public class ErrorRes {

    @Schema(description = "ErrorCode e.g(USER_NOT_FOUND")
    private ErrorCode errorCode;

    @Schema(description = "Message describing result")
    private String message;

    @Schema(description = "Details of the error")
    private String details;

    @Schema(description = "Path of the server")
    private String path;

    @Schema(description = "Timestamp of the error")
    private LocalDateTime timeStamp;

}
