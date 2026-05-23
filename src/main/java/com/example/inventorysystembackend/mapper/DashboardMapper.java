package com.example.inventorysystembackend.mapper;

import com.example.inventorysystembackend.dto.analytics.CategoryRevenueDTO;
import com.example.inventorysystembackend.dto.analytics.KPIResponseDTO;
import com.example.inventorysystembackend.dto.analytics.StockMovementTrendDTO;
import com.example.inventorysystembackend.dto.analytics.SupplierContributionDTO;
import com.example.inventorysystembackend.dto.response.DashboardSummaryResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DashboardMapper {

    public DashboardSummaryResponse toResponse(
            BigDecimal totalSale,
            Long transactionCount,
            Long lowStockCount,
            Long stockMovementCount,
            String topCategory,
            String topSupplier
    ) {

        return DashboardSummaryResponse.builder()
                .totalSales(totalSale)
                .transactionCount(transactionCount)
                .lowStockCount(lowStockCount)
                .stockMovementCount(stockMovementCount)
                .topCategory(topCategory)
                .topSupplier(topSupplier)
                .build();
    }


}
