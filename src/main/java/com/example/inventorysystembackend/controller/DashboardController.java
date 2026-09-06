package com.example.inventorysystembackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventorysystembackend.dto.response.DashboardSummaryResponse;
import com.example.inventorysystembackend.dto.shared.ResponseFactory;
import com.example.inventorysystembackend.dto.shared.response.ApiRes;
import com.example.inventorysystembackend.service.DashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Dashboard", description = "Dashboard summary metrics and KPIs")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Dashboard summary", description = "Returns summary metrics for the dashboard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard summary fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER', 'INVENTORY_CLERK', 'GUEST')")
    public ResponseEntity<ApiRes<DashboardSummaryResponse>> getDashboardSummary() {

        DashboardSummaryResponse response = dashboardService.getDashboardSummary();

        return ResponseFactory.success("Dashboard summary fetched successfully", response);
    }
}
