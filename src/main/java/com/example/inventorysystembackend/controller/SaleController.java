package com.example.inventorysystembackend.controller;

import com.example.inventorysystembackend.dto.request.SaleRequest;
import com.example.inventorysystembackend.dto.response.SaleResponse;
import com.example.inventorysystembackend.dto.shared.ResponseFactory;
import com.example.inventorysystembackend.dto.shared.response.ApiRes;
import com.example.inventorysystembackend.service.SaleService;
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

@Tag(name = "Sales", description = "Sales Management APIs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    /**
     * Creates a new sale.
     *
     * Accessible by:
     * - ADMIN
     * - CASHIER
     *
     * @param request sale registration data
     * @return created sale response
     */
    @Operation(summary = "Create sale", description = "Accessible by ADMIN AND CASHIER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sale successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    public ResponseEntity<ApiRes<SaleResponse>> createSale(@Valid @RequestBody SaleRequest request) {

        SaleResponse sale = saleService.createSale(request);

        return ResponseFactory.created("Sale created successfully", sale);
    }

    /**
     * Retrieves a sale by ID.
     *
     * Accessible by:
     * - ADMIN
     * - MANAGER
     * - CASHIER
     *
     * @param saleId sale ID
     * @return sale response
     */
    @Operation(summary = "Get sale by ID", description = "Accessible by ADMIN, MANAGER, CASHIER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Sale not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER')")
    public ResponseEntity<ApiRes<SaleResponse>> getSalesById(@PathVariable("id") Long saleId) {

        SaleResponse sale = saleService.getSaleById(saleId);

        return ResponseFactory.success("Sale fetched successfully", sale);
    }

    /**
     * Retrieves all sales.
     *
     * Accessible by:
     * - ADMIN
     * - MANAGER
     *
     * @return list of sales
     */
    @Operation(summary = "Get all sales", description = "Accessible by ADMIN AND MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sales fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiRes<List<SaleResponse>>> getAllSales() {

        List<SaleResponse> sales = saleService.getAllSales();

        return ResponseFactory.success("All sales fetched successfully", sales);
    }

    /**
     * Retrieves sales by user ID.
     *
     * Accessible by:
     * - ADMIN
     * - MANAGER
     * - CASHIER (own data only in future improvement)
     *
     * @param userId user ID
     * @return list of sales
     */
    @Operation(summary = "Get sales by user", description = "Accessible by ADMIN, MANAGER, CASHIER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User sales fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER')")
    public ResponseEntity<ApiRes<List<SaleResponse>>> getSalesByUser(@PathVariable Long userId) {

        List<SaleResponse> sales = saleService.getSalesByUser(userId);

        return ResponseFactory.success("User sales fetched successfully", sales);
    }

    /**
     * Cancels a sale.
     *
     * Accessible by:
     * - ADMIN
     * - MANAGER
     *
     * @param saleId sale ID
     * @return no content response
     */
    @Operation(summary = "Cancel sale", description = "Accessible by ADMIN AND MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sale cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Sale not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiRes<Void>> cancelSale(@PathVariable("id") Long saleId) {

        saleService.cancelSale(saleId);

        return ResponseEntity.noContent().build();
    }
}
