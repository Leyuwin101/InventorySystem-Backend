package com.example.inventorysystembackend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {

    private BigDecimal totalSales;

    private Long transactionalCount;

    private Long lowStockCount;

    private Long stockMovementCount;

    private String topCategory;

    private String topSupplier;
}
