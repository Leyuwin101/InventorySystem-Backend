package com.example.inventorysystembackend.projections;

import java.math.BigDecimal;

public interface SupplierPerformanceProjection {

    Long getSupplierId();

    String getSupplierName();

    BigDecimal getTotalContributions();

    Long getSupplierProducts();
}
