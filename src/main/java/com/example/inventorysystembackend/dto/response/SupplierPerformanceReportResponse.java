package com.example.inventorysystembackend.dto.response;

import com.example.inventorysystembackend.dto.analytics.SupplierContributionDTO;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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
