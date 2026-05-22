package com.example.inventorysystembackend.service;

import com.example.inventorysystembackend.model.entity.InventoryLogs;

import java.util.List;

public interface InventoryLogsService {

    InventoryLogs createLog(InventoryLogs inventoryLog);

    List<InventoryLogs> getAllLogs();

    List<InventoryLogs> getLogsByProduct(Long productId);

    void stockIn(Long productId, Integer quantity, String reason);

    void stockOut(Long productId, Integer quantity, String reason);

    void adjustStock(Long productId, Integer quantity, String reason);
}
