package com.example.inventorysystembackend.dto.analytics;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopCategoryDTO {

    private Long categoryId;

    private String categoryName;

    private BigDecimal totalRevenue;

    private Long totalProductsSold;
}
