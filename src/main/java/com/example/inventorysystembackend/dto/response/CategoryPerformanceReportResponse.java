package com.example.inventorysystembackend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPerformanceReportResponse {

    private BigDecimal totalRevenue;

    private Long totalProductSoLd;

    private String topCategory;

    private List<CategoryRevenueDTO> categoryRevenue;
}
