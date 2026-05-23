package com.example.inventorysystembackend.projections;

import java.math.BigDecimal;

public interface CategoryRevenueProjection {

    Long getCategoryId();

    String getCategoryName();

    BigDecimal getRevenue();

    Long getProductsSold();
}
