package com.example.inventorysystembackend.service.implement;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

import com.example.inventorysystembackend.dto.request.ReportFilterRequest;
import com.example.inventorysystembackend.dto.response.CategoryPerformanceReportResponse;
import com.example.inventorysystembackend.dto.response.InventoryMovementReportResponse;
import com.example.inventorysystembackend.dto.response.LowStockReportResponse;
import com.example.inventorysystembackend.dto.response.SalesSummaryReportResponse;
import com.example.inventorysystembackend.dto.response.SupplierPerformanceReportResponse;
import com.example.inventorysystembackend.model.enums.ExportFormat;
import com.example.inventorysystembackend.model.enums.ReportType;
import com.example.inventorysystembackend.service.ExportService;
import com.example.inventorysystembackend.service.ReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CsvExportServiceImpl implements ExportService {

    private final ReportService reportService;

    @Override
    public byte[] export(ReportType type, ExportFormat format) {
        if (format != ExportFormat.CSV) {
            throw new IllegalArgumentException("CsvExportServiceImpl only supports CSV format");
        }

        ReportFilterRequest request = ReportFilterRequest.builder().build();

        String csv;
        switch (type) {
            case SALES_SUMMARY -> csv = buildSalesSummaryCsv(reportService.getSaleSummary(request));
            case INVENTORY_SUMMARY -> csv = buildInventoryMovementCsv(reportService.getInventoryMovement(request));
            case LOW_STOCK -> csv = buildLowStockCsv(reportService.getLowStock(request));
            case CATEGORY_PERFORMANCE -> csv = buildCategoryPerformanceCsv(reportService.getCategoryPerformance(request));
            case SUPPLIER_PERFORMANCE -> csv = buildSupplierPerformanceCsv(reportService.getSupplierPerformance(request));
            default -> throw new IllegalArgumentException("Unsupported report type: " + type);
        }

        return csv.getBytes(StandardCharsets.UTF_8);
    }

    private String buildSalesSummaryCsv(SalesSummaryReportResponse summary) {
        StringBuilder builder = new StringBuilder();
        builder.append("Metric,Value\n");
        builder.append("Total Sales,").append(formatAmount(summary.getTotalSales())).append("\n");
        builder.append("Total Transactions,").append(summary.getTotalTransactions()).append("\n");
        builder.append("Average Order Value,").append(formatAmount(summary.getAveragedOrderValue())).append("\n\n");
        builder.append("Sale ID,Total Amount,Payment Method,Status,Created At\n");
        if (summary.getSales() != null) {
            summary.getSales().forEach(sale -> builder.append(escapeCsv(String.valueOf(sale.getSaleId()))).append(",")
                    .append(formatAmount(sale.getTotalAmount())).append(",")
                    .append(escapeCsv(sale.getPaymentMethod().name())).append(",")
                    .append(escapeCsv(sale.getStatus().name())).append(",")
                    .append(escapeCsv(sale.getCreatedAt().toString())).append("\n"));
        }
        return builder.toString();
    }

    private String buildInventoryMovementCsv(InventoryMovementReportResponse report) {
        StringBuilder builder = new StringBuilder();
        builder.append("Metric,Value\n");
        builder.append("Total Stock In,").append(report.getTotalStockIn()).append("\n");
        builder.append("Total Stock Out,").append(report.getTotalStockOut()).append("\n");
        builder.append("Total Adjustments,").append(report.getTotalAdjustments()).append("\n\n");
        builder.append("Inventory Log ID,Product,Type,Quantity,Reason,Created At\n");
        if (report.getLogs() != null) {
            report.getLogs().forEach(log -> builder.append(log.getInventoryLogId()).append(",")
                    .append(escapeCsv(log.getProduct().getName())).append(",")
                    .append(escapeCsv(log.getType().name())).append(",")
                    .append(log.getQuantity()).append(",")
                    .append(escapeCsv(log.getReason())).append(",")
                    .append(escapeCsv(log.getCreatedAt().toString())).append("\n"));
        }
        return builder.toString();
    }

    private String buildLowStockCsv(LowStockReportResponse response) {
        StringBuilder builder = new StringBuilder();
        builder.append("Low Stock Count,").append(response.getLowStockCount()).append("\n\n");
        builder.append("Product ID,Name,SKU,Stock Quantity,Minimum Stock\n");
        if (response.getLowStockProducts() != null) {
            response.getLowStockProducts().forEach(product -> builder.append(product.getProductId()).append(",")
                    .append(escapeCsv(product.getName())).append(",")
                    .append(escapeCsv(product.getSku())).append(",")
                    .append(product.getStockQuantity()).append(",")
                    .append(product.getMinimumStock()).append("\n"));
        }
        return builder.toString();
    }

    private String buildCategoryPerformanceCsv(CategoryPerformanceReportResponse response) {
        StringBuilder builder = new StringBuilder();
        builder.append("Total Revenue,").append(formatAmount(response.getTotalRevenue())).append("\n");
        builder.append("Total Products Sold,").append(response.getTotalProductSold()).append("\n");
        builder.append("Top Category,").append(escapeCsv(response.getTopCategory())).append("\n\n");
        builder.append("Category ID,Category Name,Revenue,Products Sold\n");
        if (response.getCategoryRevenue() != null) {
            response.getCategoryRevenue().forEach(metric -> builder.append(metric.getCategoryId()).append(",")
                    .append(escapeCsv(metric.getCategoryName())).append(",")
                    .append(formatAmount(metric.getRevenue())).append(",")
                    .append(metric.getProductsSold()).append("\n"));
        }
        return builder.toString();
    }

    private String buildSupplierPerformanceCsv(SupplierPerformanceReportResponse response) {
        StringBuilder builder = new StringBuilder();
        builder.append("Total Supplier Revenue,").append(formatAmount(response.getTotalSupplierRevenue())).append("\n");
        builder.append("Top Supplier,").append(escapeCsv(response.getTopSupplier())).append("\n\n");
        builder.append("Supplier ID,Supplier Name,Contribution Amount,Products Supplied\n");
        if (response.getSupplierContributions() != null) {
            response.getSupplierContributions().forEach(contribution -> builder.append(contribution.getSupplierId()).append(",")
                    .append(escapeCsv(contribution.getSupplierName())).append(",")
                    .append(formatAmount(contribution.getContributionAmount())).append(",")
                    .append(contribution.getSupplierProducts()).append("\n"));
        }
        return builder.toString();
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\n") || escaped.contains("\"") || escaped.contains("\r")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }

    private String formatAmount(BigDecimal amount) {
        return amount == null ? "0" : amount.toString();
    }
}
