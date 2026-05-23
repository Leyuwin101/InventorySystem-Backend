package com.example.inventorysystembackend.mapper.report;

import com.example.inventorysystembackend.dto.analytics.SupplierContributionDTO;
import com.example.inventorysystembackend.dto.response.SupplierPerformanceReportResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SupplierReportMapper {

    public SupplierPerformanceReportResponse toResponse(
            BigDecimal totalSupplierRevenue,
            String topSupplier,
            List<SupplierContributionDTO> supplierContributions
    ) {

        return SupplierPerformanceReportResponse.builder()
                .totalSupplierRevenue(totalSupplierRevenue)
                .topSupplier(topSupplier)
                .supplierContributions(supplierContributions)
                .build();
    }
}
