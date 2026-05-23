package com.example.inventorysystembackend.service;

import com.example.inventorysystembackend.dto.request.InventoryLogFilterRequest;
import com.example.inventorysystembackend.dto.response.InventoryLogResponse;
import com.example.inventorysystembackend.dto.response.PaginatedInventoryLogsResponse;

public interface InventoryLogsService {

    PaginatedInventoryLogsResponse getLogs(InventoryLogFilterRequest request);

    InventoryLogResponse createStockIn(Long productId, Integer quantity, String reason);

    InventoryLogResponse createStockOut(Long productId, Integer quantity, String reason);

    InventoryLogResponse adjustStock(Long productid, Integer quantity, String reason);
}
