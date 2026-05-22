package com.example.inventorysystembackend.mapper;

import com.example.inventorysystembackend.dto.request.UserRequest;
import com.example.inventorysystembackend.dto.response.UserResponse;
import com.example.inventorysystembackend.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        return user;
    }

    public UserResponse toDTO(User user) {

        return new UserResponse(
                user.getUserID(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
