package com.example.inventorysystembackend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierPerformanceReportResponse {

    private BigDecimal totalSupplierRevenue;

    private String topSupplier;

    private List<SupplierContributionDTO> supplierContributions;
}
