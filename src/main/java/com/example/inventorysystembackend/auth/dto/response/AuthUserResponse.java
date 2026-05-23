package com.example.inventorysystembackend.auth.dto.response;

import com.example.inventorysystembackend.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User details")
public class AuthUserResponse {

    @Schema(description = "ID of the user")
    private Long userId;

    @Schema(description = "Email of the user")
    private String email;

    @Schema(description = "Username of the user")
    private String username;

    @Schema(description = "Display name of the user")
    private String name;


    @Schema(description = "Role of the user")
    private Role role;
}
