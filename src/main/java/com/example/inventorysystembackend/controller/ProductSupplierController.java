package com.example.inventorysystembackend.controller;

import com.example.inventorysystembackend.dto.request.ProductSupplierRequest;
import com.example.inventorysystembackend.dto.response.ProductResponse;
import com.example.inventorysystembackend.dto.shared.ResponseFactory;
import com.example.inventorysystembackend.dto.shared.response.ApiRes;
import com.example.inventorysystembackend.service.ProductSupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Products Suppliers", description = "Product-Supplier Relationship APIs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductSupplierController {

    private final ProductSupplierService productSupplierService;

    /**
     * Assigns a supplier to a product.
     *
     * Business behavior:
     * - Creates relationship if not existing
     * - Updates supplier price/lead time if already existing
     *
     * Accessible by:
     * - ADMIN
     * - MANAGER
     *
     * @param productId product ID
     * @param request supplier assignment data
     * @return updated product response
     */
    @Operation(summary = "Assign supplier to product", description = "Accessible by ADMIN and MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Product or supplier not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("/{id}/suppliers")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiRes<ProductResponse>> assignSupplier(
            @PathVariable("id") Long productId,
            @Valid @RequestBody ProductSupplierRequest request
    ) {

        ProductResponse updated = productSupplierService.assignSupplier(productId, request);

        return ResponseFactory.success("Supplier assigned successfully", updated);
    }

    /**
     * Removes a supplier from a product.
     *
     * Accessible by:
     * - ADMIN
     * - MANAGER
     *
     * @param productId product ID
     * @param supplierId supplier ID
     * @return updated product response
     */
    @Operation(
            summary = "Remove supplier from product",
            description = "Accessible by ADMIN and MANAGER"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier removed successfully"),
            @ApiResponse(responseCode = "404", description = "Product, supplier, or relationship not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{productId}/suppliers/{supplierId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiRes<ProductResponse>> removeSupplier(
            @PathVariable Long productId,
            @PathVariable Long supplierId
    ) {

        ProductResponse updated = productSupplierService.removeSupplier(productId, supplierId);

        return ResponseFactory.success("Supplier removed successfully", updated);
    }

}
