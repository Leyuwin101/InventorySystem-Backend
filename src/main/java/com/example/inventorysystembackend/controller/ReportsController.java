package com.example.inventorysystembackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventorysystembackend.dto.request.ReportFilterRequest;
import com.example.inventorysystembackend.dto.response.CategoryPerformanceReportResponse;
import com.example.inventorysystembackend.dto.response.InventoryMovementReportResponse;
import com.example.inventorysystembackend.dto.response.LowStockReportResponse;
import com.example.inventorysystembackend.dto.response.SalesSummaryReportResponse;
import com.example.inventorysystembackend.dto.response.SupplierPerformanceReportResponse;
import com.example.inventorysystembackend.dto.shared.ResponseFactory;
import com.example.inventorysystembackend.dto.shared.response.ApiRes;
import com.example.inventorysystembackend.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Reports", description = "Analytical and reporting endpoints")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final ReportService reportService;

    @Operation(summary = "Sales summary report", description = "Returns aggregated sales summary data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sales summary fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/sales-summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER', 'INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<SalesSummaryReportResponse>> getSalesSummary(
            @Valid @ModelAttribute ReportFilterRequest request
    ) {
        SalesSummaryReportResponse response = reportService.getSaleSummary(request);
        return ResponseFactory.success("Sales summary fetched successfully", response);
    }

    @Operation(summary = "Inventory movement report", description = "Returns stock movement summary and logs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory movement report fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/inventory-movement")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER', 'INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<InventoryMovementReportResponse>> getInventoryMovement(
            @Valid @ModelAttribute ReportFilterRequest request
    ) {
        InventoryMovementReportResponse response = reportService.getInventoryMovement(request);
        return ResponseFactory.success("Inventory movement report fetched successfully", response);
    }

    @Operation(summary = "Low stock report", description = "Returns products with inventory below configured minimums")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Low stock report fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER', 'INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<LowStockReportResponse>> getLowStock(
            @Valid @ModelAttribute ReportFilterRequest request
    ) {
        LowStockReportResponse response = reportService.getLowStock(request);
        return ResponseFactory.success("Low stock report fetched successfully", response);
    }

    @Operation(summary = "Category performance report", description = "Returns category revenue and performance data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category performance report fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/category-performance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER', 'INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<CategoryPerformanceReportResponse>> getCategoryPerformance(
            @Valid @ModelAttribute ReportFilterRequest request
    ) {
        CategoryPerformanceReportResponse response = reportService.getCategoryPerformance(request);
        return ResponseFactory.success("Category performance report fetched successfully", response);
    }

    @Operation(summary = "Supplier performance report", description = "Returns supplier contribution and performance data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier performance report fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/supplier-performance")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER', 'INVENTORY_CLERK')")
    public ResponseEntity<ApiRes<SupplierPerformanceReportResponse>> getSupplierPerformance(
            @Valid @ModelAttribute ReportFilterRequest request
    ) {
        SupplierPerformanceReportResponse response = reportService.getSupplierPerformance(request);
        return ResponseFactory.success("Supplier performance report fetched successfully", response);
    }
}
