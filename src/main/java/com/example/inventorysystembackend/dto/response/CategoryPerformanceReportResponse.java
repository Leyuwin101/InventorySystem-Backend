package com.example.inventorysystembackend.dto.response;

import com.example.inventorysystembackend.dto.analytics.CategoryRevenueDTO;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPerformanceReportResponse {

    private BigDecimal totalRevenue;

    private Long totalProductSold;

    private String topCategory;

    private List<CategoryRevenueDTO> categoryRevenue;
}
