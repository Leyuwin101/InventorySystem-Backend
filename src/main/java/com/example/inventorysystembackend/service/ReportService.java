package com.example.inventorysystembackend.service;

import com.example.inventorysystembackend.dto.request.ReportFilterRequest;
import com.example.inventorysystembackend.dto.response.*;
import com.example.inventorysystembackend.model.entity.Supplier;

public interface ReportService {

    SalesSummaryReportResponse getSaleSummary(ReportFilterRequest request);

    InventoryMovementReportResponse getInventoryMovement(ReportFilterRequest request);

    LowStockReportResponse getLowStock(ReportFilterRequest request);

    CategoryPerformanceReportResponse getCategoryPerformance(ReportFilterRequest request);

    SupplierPerformanceReportResponse getSupplierPerformance(ReportFilterRequest request);

}
