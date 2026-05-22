package com.example.inventorysystembackend.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Login Request")
public class AuthRequest {

    @Schema(example = "admin@gmail.com")
    private String email;

    @Schema(example = "admin123")
    private String password;
}
