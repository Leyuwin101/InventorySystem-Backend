package com.example.inventorysystembackend.dto.analytics;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRevenueDTO {

    private Long categoryId;

    private String categoryName;

    private BigDecimal revenue;

    private Long productsSold;

    private Double revenuePercentage;
}
