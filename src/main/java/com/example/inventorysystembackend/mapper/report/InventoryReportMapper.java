package com.example.inventorysystembackend.mapper.report;

import com.example.inventorysystembackend.dto.analytics.StockMovementTrendDTO;
import com.example.inventorysystembackend.dto.response.InventoryMovementReportResponse;
import com.example.inventorysystembackend.dto.response.LowStockReportResponse;
import com.example.inventorysystembackend.mapper.InventoryLogsMapper;
import com.example.inventorysystembackend.mapper.ProductMapper;
import com.example.inventorysystembackend.model.entity.InventoryLogs;
import com.example.inventorysystembackend.model.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InventoryReportMapper {

    private final InventoryLogsMapper inventoryLogsMapper;
    private final ProductMapper productMapper;

    public InventoryMovementReportResponse toInventoryMovementResponse(
            Long totalStockIn,
            Long totalStockOut,
            Long totalAdjustments,
            List<StockMovementTrendDTO> movementTrend,
            List<InventoryLogs> logs
    ) {

        return InventoryMovementReportResponse.builder()
                        .totalStockIn(totalStockIn)
                        .totalStockOut(totalStockOut)
                        .totalAdjustments(totalAdjustments)
                        .movementTrend(movementTrend)
                        .logs(
                                logs.stream()
                                        .map(inventoryLogsMapper::toDTO)
                                        .toList()
                        )
                        .build();

    }


    public LowStockReportResponse toLowStockReportResponse(
            Long lowStockCount,
            List<Product> products
    ) {

        return LowStockReportResponse.builder()
                .lowStockCount(lowStockCount)
                .lowStockProducts(
                        products.stream()
                                .map(productMapper::toDTO)
                                .toList()
                )
                .build();
    }
}
