package com.example.inventorysystembackend.controller;

import com.example.inventorysystembackend.dto.request.UserRequest;
import com.example.inventorysystembackend.dto.response.UserResponse;
import com.example.inventorysystembackend.dto.shared.ResponseFactory;
import com.example.inventorysystembackend.dto.shared.response.ApiRes;
import com.example.inventorysystembackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "User management APIs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController{

    private final UserService userService;

    /// | Role            | Permissions                    |
    /// | --------------- | ------------------------------ |
    /// | ADMIN           | Full access                    |
    /// | MANAGER         | Manage products/orders/reports |
    /// | CASHIER         | Sales transactions only        |
    /// | INVENTORY_CLERK | Inventory stock management     |
    /// | GUEST           | Read-only system browsing      |

    /**
     * Creates a new user account.
     *
     * Accessible only by ADMIN users.
     *
     * @param request user registration data
     * @return created user response
     */
    @Operation(summary = "Create new user", description = "Accessible by ADMIN only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiRes<UserResponse>> createUser(@Valid @RequestBody UserRequest request) {

        UserResponse user = userService.createUser(request);

        return ResponseFactory.created("User created successfully", user);
    }

    /**
     * Updates an existing user account.
     *
     * Accessible only by ADMIN users.
     *
     * @param userId user ID to update
     * @param request updated user data
     * @return updated user response
     */
    @Operation(summary = "Update user by ID", description = "Accessible by ADMIN only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiRes<UserResponse>> updateUser(
            @PathVariable("id") Long userId,
            @Valid @RequestBody UserRequest request
    ) {

        UserResponse updated = userService.updateUser(userId, request);

        return ResponseFactory.success("User updated", updated);
    }

    /**
     * Delete user using delete mapping
     *
     * - Do it using Postman Delete
     *
     * @param userId to delete
     * @return no Content
     */
    @Operation(summary = "Delete user by ID", description = "Accessible by ADMIN only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiRes<Void>> deleteUser(@PathVariable("id") Long userId) {

        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();

    }

    /**
     * Retrieves a single user by ID.
     *
     * Accessible by ADMIN and MANAGER roles.
     *
     * @param userId user ID to retrieve
     * @return user response
     */
    @Operation(summary = "Get user by ID", description = "Accessible by ADMIN and MANAGER only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User fetched successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'GUEST')")
    public ResponseEntity<ApiRes<UserResponse>> getUserById(@PathVariable("id") Long userId) {

        UserResponse user = userService.getUserById(userId);

        return ResponseFactory.success("User fetched", user);
    }

    /**
     * Retrieves all registered users.
     *
     * Accessible only by ADMIN users.
     *
     * @return list of users
     */
    @Operation(summary = "Get all users", description = "Accessible by ADMIN only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GUEST')")
    public ResponseEntity<ApiRes<List<UserResponse>>> getAllUsers() {

        List<UserResponse> responses = userService.getAllUsers();

        return ResponseFactory.success("All users fetched", responses);
    }



}
