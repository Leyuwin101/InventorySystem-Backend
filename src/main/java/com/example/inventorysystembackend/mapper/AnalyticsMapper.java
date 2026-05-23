package com.example.inventorysystembackend.mapper;

import com.example.inventorysystembackend.dto.analytics.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AnalyticsMapper {

    public SalesTrendDTO toSalesTrendDTO(
            String date,
            BigDecimal sales,
            Long transactions
    ) {

        return SalesTrendDTO.builder()
                .date(date)
                .sales(sales)
                .transactions(transactions)
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
