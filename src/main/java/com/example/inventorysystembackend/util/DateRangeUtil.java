package com.example.inventorysystembackend.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateRangeUtil {

    public static LocalDateTime startOfDay(LocalDate date) {
        return date == null ? null : date.atStartOfDay();
    }

    public static LocalDateTime endOfDay(LocalDate date) {
        return date == null ? null : date.atTime(23, 59, 59);
    }
}
