package com.example.inventorysystembackend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSupplierRequest {

    @NotNull(message = "Supplier Id is required")
    private Long supplierId;

    @NotNull(message = "Supplier price is required")
    private BigDecimal supplierPrice;

    @NotNull(message = "Lead time days is required")
    private Integer leadTimeDays;
}
