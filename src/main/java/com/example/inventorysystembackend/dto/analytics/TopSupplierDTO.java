package com.example.inventorysystembackend.dto.analytics;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopSupplierDTO {

    private Long supplierId;

    private String supplierName;

    private BigDecimal totalContribution;

    private Long totalProductsSupplied;
}
