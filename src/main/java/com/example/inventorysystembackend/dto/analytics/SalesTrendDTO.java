package com.example.inventorysystembackend.dto.analytics;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesTrendDTO {

    private String date;

    private BigDecimal sales;

    private Long transactions;
}
