package com.example.inventorysystembackend.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Refresh token request")
public class RefreshRequest {

    private String refreshToken;
}
