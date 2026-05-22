package com.example.inventorysystembackend.controller;

import com.example.inventorysystembackend.dto.request.SupplierRequest;
import com.example.inventorysystembackend.dto.response.SupplierResponse;
import com.example.inventorysystembackend.dto.shared.ResponseFactory;
import com.example.inventorysystembackend.dto.shared.response.ApiRes;
import com.example.inventorysystembackend.service.SupplierService;
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

@Tag(name = "Supplier", description = "Supplier Management APIs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    /**
     * Creates a new supplier.
     *
     * Accessible by ADMIN and MANAGER roles only.
     *
     * @param request supplier registration data
     * @return created supplier response
     */
    @Operation(summary = "Create supplier", description = "Accessible by ADMIN and MANAGER only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiRes<SupplierResponse>> createSupplier(@Valid @RequestBody SupplierRequest request) {

        SupplierResponse supplier = supplierService.createSupplier(request);

        return ResponseFactory.created("Supplier created successfully", supplier);
    }


    /**
     * Updates an existing supplier account.
     *
     * Accessible only by ADMIN and MANAGER roles only.
     *
     * @param supplierId supplier ID to update
     * @param request updated supplier data
     * @return updated supplier response
     */
    @Operation(summary = "Update Supplier", description = "Accessible by ADMIN and MANAGER only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier successfully updated"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiRes<SupplierResponse>> updateSupplier(
            @PathVariable("id") Long supplierId,
            @Valid @RequestBody SupplierRequest request
    ) {

        SupplierResponse updated = supplierService.updateSupplier(supplierId, request);

        return ResponseFactory.success("Supplier updated", updated);
    }

    /**
     * Deletes a supplier by ID.
     *
     * Accessible only by ADMIN users.
     *
     * @param supplierId supplier ID to delete
     * @return empty response with HTTP 204 status
     */
    @Operation(summary = "Delete supplier", description = "Accessible by ADMIN only")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supplier deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiRes<Void>> deleteSupplier(@PathVariable("id") Long supplierId) {

        supplierService.deleteSupplier(supplierId);

        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves a supplier by ID.
     *
     * Accessible by ADMIN, MANAGER, INVENTORY_CLERK roles only
     *
     * @param supplierId supplier ID to retrieve
     * @return supplier response
     */
    @Operation(summary = "Get supplier by ID", description = "Accessible by ADMIN, MANAGER, INVENTORY_CLERK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<SupplierResponse>> getSupplierById(@PathVariable("id") Long supplierId) {

        SupplierResponse supplier = supplierService.getSupplierById(supplierId);

        return ResponseFactory.success("Supplier fetched", supplier);
    }

    /**
     * Retrieves all available suppliers.
     *
     * Accessible by ADMIN, MANAGER, INVENTORY_CLERK roles only
     *
     * @return list of suppliers
     */
    @Operation(summary = "Get all suppliers", description = "Accessible by ADMIN, MANAGER, INVENTORY_CLERK")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<List<SupplierResponse>>> getAllSuppliers() {

        List<SupplierResponse> suppliers = supplierService.getAllSuppliers();

        return ResponseFactory.success("All suppliers fetched", suppliers);
    }
}
