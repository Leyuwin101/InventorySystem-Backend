package com.example.inventorysystembackend.dto.response;

import com.example.inventorysystembackend.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long userId;

    private String username;

    private String email;

    private Role role;

    private LocalDateTime createdAt;

}
