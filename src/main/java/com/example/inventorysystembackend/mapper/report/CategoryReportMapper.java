package com.example.inventorysystembackend.mapper.report;

import com.example.inventorysystembackend.dto.analytics.CategoryRevenueDTO;
import com.example.inventorysystembackend.dto.response.CategoryPerformanceReportResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CategoryReportMapper {

    public CategoryPerformanceReportResponse toResponse(
            BigDecimal totalRevenue,
            Long totalProductsSold,
            String topCategory,
            List<CategoryRevenueDTO> categoryRevenue
    ) {

        return CategoryPerformanceReportResponse.builder()
                .totalRevenue(totalRevenue)
                .totalProductSold(totalProductsSold)
                .topCategory(topCategory)
                .categoryRevenue(categoryRevenue)
                .build();
    }
}
