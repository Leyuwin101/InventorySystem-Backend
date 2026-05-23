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


    public StockMovementTrendDTO toStockMovementTrendDTO(
            String date,
            Long stockIn,
            Long stockOut,
            Long adjustments
    ) {

        return StockMovementTrendDTO.builder()
                .date(date)
                .stockIn(stockIn)
                .stockOut(stockOut)
                .adjustments(adjustments)
                .build();
    }


    public CategoryRevenueDTO toCategoryRevenueDTO(
            Long categoryId,
            String categoryName,
            BigDecimal revenue,
            Long productSold,
            Double revenuePercentage
    ) {

        return CategoryRevenueDTO.builder()
                .categoryId(categoryId)
                .categoryName(categoryName)
                .revenue(revenue)
                .productsSold(productSold)
                .revenuePercentage(revenuePercentage)
                .build();
    }

    public SupplierContributionDTO toSupplierContributionDTO(
            Long supplierId,
            String supplierName,
            BigDecimal contributionAmount,
            Double contributionPercentage,
            Long supplierProducts
    ) {

        return SupplierContributionDTO.builder()
                .supplierId(supplierId)
                .supplierName(supplierName)
                .contributionAmount(contributionAmount)
                .contributionPercentage(contributionPercentage)
                .supplierProducts(supplierProducts)
                .build();
    }


    public KPIResponseDTO toKpiDTO(
            String title,
            String value,
            String description,
            Double percentageChange
    ) {

        return KPIResponseDTO.builder()
                .title(title)
                .value(value)
                .description(description)
                .percentageChange(percentageChange)
                .build();
    }
}
