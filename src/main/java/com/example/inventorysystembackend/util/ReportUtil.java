package com.example.inventorysystembackend.util;

import java.math.BigDecimal;

public class ReportUtil {

    public static BigDecimal safeDivide(BigDecimal total, BigDecimal count) {
        if (count == null || count.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return total.divide(count, 2, BigDecimal.ROUND_HALF_UP);
    }

    public static Double percentage(double part, double total) {
        if (total == 0) return 0.0;
        return (part / total) * 100;
    }
}
