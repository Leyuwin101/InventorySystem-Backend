package com.example.inventorysystembackend.dto.analytics;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierContributionDTO {

    private Long supplierId;

    private String supplierName;

    private BigDecimal contributionAmount;

    private Double contributionPercentage;

    private Long supplierProducts;
}
