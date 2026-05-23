package com.example.inventorysystembackend.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LowStockReportResponse {

    private Long lowStockCount;

    private List<ProductResponse> lowStockProducts;
}
