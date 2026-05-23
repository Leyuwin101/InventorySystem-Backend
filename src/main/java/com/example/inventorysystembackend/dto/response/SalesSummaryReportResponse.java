package com.example.inventorysystembackend.dto.response;

import com.example.inventorysystembackend.dto.analytics.SalesTrendDTO;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesSummaryReportResponse {

    private BigDecimal totalSales;

    private Long totalTransactions;

    private BigDecimal averagedOrderValue;

    private List<SalesTrendDTO> salesTrend;

    private List<SaleResponse> sales;
}
