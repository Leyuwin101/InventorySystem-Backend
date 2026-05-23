package com.example.inventorysystembackend.projections;

import java.math.BigDecimal;

public interface DashboardSummaryProjection {
    BigDecimal getTotalSales();

    Long getTransactionCount();

    Long getLowStockCount();

    Long getStockMovementCount();

    String getTopCategory();

    String getTopSupplier();
}
