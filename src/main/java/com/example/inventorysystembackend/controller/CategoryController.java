package com.example.inventorysystembackend.controller;

import com.example.inventorysystembackend.dto.request.CategoryRequest;
import com.example.inventorysystembackend.dto.response.CategoryResponse;
import com.example.inventorysystembackend.dto.shared.ResponseFactory;
import com.example.inventorysystembackend.dto.shared.response.ApiRes;
import com.example.inventorysystembackend.service.CategoryService;
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

@Tag(name = "Categories", description = "Categories Management APIs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Creates a new product category.
     *
     * Accessible by:
     * - ADMIN
     * - MANAGER
     * - INVENTORY_CLERK
     *
     * @param request category registration data
     * @return created category response
     */
    @Operation(summary = "Create category", description = "Accessible by ADMIN, MANAGER, and INVENTORY_CLERK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request) {

        CategoryResponse category = categoryService.createCategory(request);

        return ResponseFactory.created("Category Created Successfully", category);
    }


    /**
     * Updates an existing category.
     *
     * Accessible by:
     * - ADMIN
     * - MANAGER
     * - INVENTORY_CLERK
     *
     * @param categoryId category ID to update
     * @param request updated category data
     * @return updated category response
     */
    @Operation(summary = "Update category", description = "Accessible by ADMIN, MANAGER, and INVENTORY_CLERK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<CategoryResponse>> updateCategory(
            @PathVariable("id") Long categoryId,
            @Valid @RequestBody CategoryRequest request) {

        CategoryResponse updated = categoryService.updateCategory(categoryId, request);

        return ResponseFactory.success("Category updated successfully", updated);
    }

    /**
     * Deletes a category by ID.
     *
     * Accessible only by ADMIN users.
     *
     * @param categoryId category ID to delete
     * @return empty response with HTTP 204 status
     */
    @Operation(summary = "Delete category", description = "Accessible by ADMIN only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiRes<Void>> deleteCategory(@PathVariable("id") Long categoryId) {

        categoryService.deleteCategory(categoryId);

        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves a category by ID.
     *
     * Accessible by all authenticated users.
     *
     * @param categoryId category ID to retrieve
     * @return category response
     */
    @Operation(summary = "Get category by ID", description = "Accessible by all authenticated users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER', 'INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<CategoryResponse>> getCategoryById(@PathVariable("id") Long categoryId) {

        CategoryResponse category = categoryService.getCategoryById(categoryId);

        return ResponseFactory.success("Category Fetched", category);
    }

    /**
     * Retrieves all available categories.
     *
     * Accessible by all authenticated users.
     *
     * @return list of categories
     */
    @Operation(summary = "Get all categories", description = "Accessible by all authenticated users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER', 'INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<List<CategoryResponse>>> getAllCategories() {

        List<CategoryResponse> categories = categoryService.getAllCategories();

        return ResponseFactory.success("All categories fetched", categories);
    }


}
