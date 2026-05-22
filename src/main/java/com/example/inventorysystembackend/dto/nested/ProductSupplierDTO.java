package com.example.inventorysystembackend.dto.nested;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSupplierDTO {

    private Long supplierId;

    private String supplierName;

    private String companyName;

    private BigDecimal supplierPrice;

    private Integer leadTimeDays;



}
