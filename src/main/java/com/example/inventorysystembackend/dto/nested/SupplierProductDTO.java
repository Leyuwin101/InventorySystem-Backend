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
public class SupplierProductDTO {

    private Long productId;

    private String productName;

    private String sku;

    private BigDecimal supplierPrice;

    private Integer leadTimeDays;
}
