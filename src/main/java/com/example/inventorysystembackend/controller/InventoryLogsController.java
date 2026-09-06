package com.example.inventorysystembackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventorysystembackend.dto.request.InventoryLogFilterRequest;
import com.example.inventorysystembackend.dto.request.InventoryLogRequest;
import com.example.inventorysystembackend.dto.response.InventoryLogResponse;
import com.example.inventorysystembackend.dto.response.PaginatedInventoryLogsResponse;
import com.example.inventorysystembackend.dto.shared.ResponseFactory;
import com.example.inventorysystembackend.dto.shared.response.ApiRes;
import com.example.inventorysystembackend.service.InventoryLogsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Inventory Logs", description = "Endpoints for managing product inventory logs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inventory-logs")
public class InventoryLogsController {

    private final InventoryLogsService inventoryLogsService;

    @Operation(summary = "Query inventory logs", description = "Returns paginated inventory log entries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory logs fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER', 'INVENTORY_CLERK', 'GUEST')")
    public ResponseEntity<ApiRes<PaginatedInventoryLogsResponse>> getInventoryLogs(
            @Valid @ModelAttribute InventoryLogFilterRequest request
    ) {

        PaginatedInventoryLogsResponse response = inventoryLogsService.getLogs(request);

        return ResponseFactory.success("Inventory logs fetched successfully", response);
    }

    @Operation(summary = "Create stock in entry", description = "Registers stock received into inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stock-in log created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("/stock-in")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<InventoryLogResponse>> stockIn(@Valid @RequestBody InventoryLogRequest request) {

        InventoryLogResponse response = inventoryLogsService.createStockIn(request.getProductId(), request.getQuantity(), request.getReason());

        return ResponseFactory.created("Stock-in created successfully", response);
    }

    @Operation(summary = "Create stock out entry", description = "Registers stock issued out of inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stock-out log created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("/stock-out")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<InventoryLogResponse>> stockOut(@Valid @RequestBody InventoryLogRequest request) {

        InventoryLogResponse response = inventoryLogsService.createStockOut(request.getProductId(), request.getQuantity(), request.getReason());

        return ResponseFactory.created("Stock-out created successfully", response);
    }

    @Operation(summary = "Create stock adjustment entry", description = "Registers an inventory adjustment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventory adjustment created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("/adjust")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<InventoryLogResponse>> adjustStock(@Valid @RequestBody InventoryLogRequest request) {

        InventoryLogResponse response = inventoryLogsService.adjustStock(request.getProductId(), request.getQuantity(), request.getReason());

        return ResponseFactory.created("Inventory adjustment created successfully", response);
    }
}
