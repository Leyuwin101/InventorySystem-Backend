package com.example.inventorysystembackend.controller;

import com.example.inventorysystembackend.dto.request.ProductRequest;
import com.example.inventorysystembackend.dto.response.ProductResponse;
import com.example.inventorysystembackend.dto.shared.ResponseFactory;
import com.example.inventorysystembackend.dto.shared.response.ApiRes;
import com.example.inventorysystembackend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.inventorysystembackend.dto.request.UpdateStockRequest;

import java.util.List;

@Tag(name = "Products", description = "Product Management APIs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /**
     * Creates a new product .
     *
     * Accessible by:
     * - ADMIN
     * - MANAGER
     * - INVENTORY_CLERK
     *
     * @param request product registration data
     * @return created product response
     */
    @Operation(summary = "Create product", description = "Accessible by ADMIN, MANAGER, and INVENTORY_CLERK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {

        ProductResponse product = productService.createProduct(request);

        return ResponseFactory.created("Product created", product);
    }

    /**
     * Updates an existing product.
     *
     * Accessible by:
     * - ADMIN
     * - MANAGER
     * - INVENTORY_CLERK
     *
     * @param productId product ID to update
     * @param request updated product data
     * @return updated product response
     */
    @Operation(summary = "Update Product", description = "Accessible by ADMIN, MANAGER, and INVENTORY_CLERK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<ProductResponse>> updateProduct(
            @PathVariable("id") Long productId,
            @Valid @RequestBody ProductRequest request
    ) {

        ProductResponse updated = productService.updateProduct(productId, request);

        return ResponseFactory.success("Updated Product", updated);
    }


    /**
     * Deletes a product by ID.
     *
     * Accessible only by ADMIN users.
     *
     * @param productId product ID to delete
     * @return empty response with HTTP 204 status
     */
    @Operation(summary = "Delete Product", description = "Accessible by ADMIN only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiRes<Void>> deleteProduct(@PathVariable("id") Long productId) {

        productService.deleteProduct(productId);

        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves a product by ID.
     *
     * Accessible by all authenticated users.
     *
     * @param productId product ID to retrieve
     * @return product response
     */
    @Operation(summary = "Get Product by ID", description = "Accessible by all authenticated users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER', 'INVENTORY_CLERK', 'GUEST')")
    public ResponseEntity<ApiRes<ProductResponse>> getProductById(@PathVariable("id") Long productId) {

        ProductResponse product = productService.getProductById(productId);

        return ResponseFactory.success("Product fetched successfully", product);
    }


    /**
     * Retrieves all available products.
     *
     * Accessible by all authenticated users.
     *
     * @return list of products
     */
    @Operation(summary = "Get all products", description = "Accessible by all authenticated users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "products fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER', 'INVENTORY_CLERK', 'GUEST')")
    public ResponseEntity<ApiRes<List<ProductResponse>>> getAllProducts() {

        List<ProductResponse> products = productService.getAllProducts();

        return ResponseFactory.success("All Products fetched successfully", products);
    }

    /**
     * Updates product stock quantity.
     *
     * Accessible by:
     * - ADMIN
     * - MANAGER
     * - INVENTORY_CLERK
     *
     * @param productId product ID
     * @param request stock update request
     * @return updated product response
     */
    @Operation(summary = "Update Product Stock", description = "Accessible by ADMIN, MANAGER, and INVENTORY_CLERK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid stock adjustment"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<ProductResponse>> updateStock(
            @PathVariable("id") Long productId,
            @Valid @RequestBody UpdateStockRequest request
    ) {

        ProductResponse updated = productService.updateStock(productId, request.getQuantity());

        return ResponseFactory.success("Stock updated successfully", updated);
    }

    /**
     * Retrieves all products with low stock.
     *
     * Business rule:
     * - Product is low stock when stockQuantity <= minimumStock
     *
     * Accessible by:
     * - ADMIN
     * - MANAGER
     * - INVENTORY_CLERK
     *
     * @return list of low stock products
     */
    @Operation(summary = "Get Low Stock Products", description = "Accessible by ADMIN, MANAGER, and INVENTORY_CLERK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Low stock products retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'INVENTORY_CLERK', 'GUEST')")
    public ResponseEntity<ApiRes<List<ProductResponse>>> getLowStocksProducts() {

        List<ProductResponse> products = productService.getLowStockProduct();

        return ResponseFactory.success("Low stocks products fetched successfully", products);
    }
}
