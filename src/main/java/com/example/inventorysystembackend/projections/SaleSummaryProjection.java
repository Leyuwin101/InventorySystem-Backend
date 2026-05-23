package com.example.inventorysystembackend.projections;

import java.math.BigDecimal;

public interface SaleSummaryProjection {

    BigDecimal getTotalSales();

    Long getTotalTransactions();

    BigDecimal getAverageOrderValue();
}
