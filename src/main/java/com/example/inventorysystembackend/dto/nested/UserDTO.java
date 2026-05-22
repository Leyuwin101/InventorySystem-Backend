package com.example.inventorysystembackend.dto.nested;

import com.example.inventorysystembackend.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {

    private Long userId;

    private String username;

    private Role role;
}
