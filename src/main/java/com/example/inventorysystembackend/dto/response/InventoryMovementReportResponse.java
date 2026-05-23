package com.example.inventorysystembackend.dto.response;

import com.example.inventorysystembackend.dto.analytics.StockMovementTrendDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryMovementReportResponse {

    private Long totalStockIn;

    private Long totalStockOut;

    private Long totalAdjustments;

    private List<StockMovementTrendDTO> movementTrend;

    private List<InventoryLogResponse> logs;
}
