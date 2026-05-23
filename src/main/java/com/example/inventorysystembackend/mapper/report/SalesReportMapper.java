package com.example.inventorysystembackend.mapper.report;

import com.example.inventorysystembackend.dto.analytics.SalesTrendDTO;
import com.example.inventorysystembackend.dto.response.SalesSummaryReportResponse;
import com.example.inventorysystembackend.mapper.SaleMapper;
import com.example.inventorysystembackend.model.entity.Sale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SalesReportMapper {

    private final SaleMapper saleMapper;

    public SalesSummaryReportResponse toResponse(
            BigDecimal totalSales,
            Long totalTransactions,
            BigDecimal averageOrderValue,
            List<SalesTrendDTO> saleTrend,
            List<Sale> sales
    ) {

        return SalesSummaryReportResponse.builder()
                .totalSales(totalSales)
                .totalTransactions(totalTransactions)
                .averagedOrderValue(averageOrderValue)
                .salesTrend(saleTrend)
                .sales(
                        sales.stream()
                                .map(saleMapper::toDTO)
                                .toList()
                )
                .build();
    }
}
