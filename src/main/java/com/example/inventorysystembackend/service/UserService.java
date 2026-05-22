package com.example.inventorysystembackend.service;

import com.example.inventorysystembackend.dto.request.UserRequest;
import com.example.inventorysystembackend.dto.response.UserResponse;

import java.util.List;


public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse updateUser(Long userId, UserRequest request);

    void deleteUser(Long userId);

    UserResponse getUserById(Long userId);

    List<UserResponse> getAllUsers();

}
