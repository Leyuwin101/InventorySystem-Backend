package com.example.inventorysystembackend.mapper;

import com.example.inventorysystembackend.dto.analytics.SalesTrendDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AnalyticsMapper {

    public SalesTrendDTO toSalesTrendDTO(
            String date,
            BigDecimal sales,
            Long transactions
    ) {

        return SalesTrendDTO.builder()
                .date(date)
                .sales(sales)
                .transactions(transactions)
                .build();
    }



}
