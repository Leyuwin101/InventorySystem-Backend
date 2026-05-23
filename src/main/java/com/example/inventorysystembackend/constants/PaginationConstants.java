package com.example.inventorysystembackend.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaginationConstants {

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_LIMIT = 10;

    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "desc";

    public static final int MAX_LIMIT = 100;
}
