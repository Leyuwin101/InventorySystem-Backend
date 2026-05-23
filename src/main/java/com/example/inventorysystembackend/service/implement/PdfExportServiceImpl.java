package com.example.inventorysystembackend.service.implement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
public class PdfExportServiceImpl implements ExportService {

    private final ReportService reportService;

    @Override
    public byte[] export(ReportType type, ExportFormat format) {
        Objects.requireNonNull(type, "Report type is required");
        Objects.requireNonNull(format, "Export format is required");

        if (format != ExportFormat.PDF) {
            throw new IllegalArgumentException("PdfExportServiceImpl only supports PDF format");
        }

        ReportFilterRequest request = ReportFilterRequest.builder().build();

        List<String> lines = switch (type) {
            case SALES_SUMMARY -> buildSalesSummaryLines(reportService.getSaleSummary(request));
            case INVENTORY_SUMMARY -> buildInventoryMovementLines(reportService.getInventoryMovement(request));
            case LOW_STOCK -> buildLowStockLines(reportService.getLowStock(request));
            case CATEGORY_PERFORMANCE -> buildCategoryPerformanceLines(reportService.getCategoryPerformance(request));
            case SUPPLIER_PERFORMANCE -> buildSupplierPerformanceLines(reportService.getSupplierPerformance(request));
            default -> throw new IllegalArgumentException("Unsupported report type: " + type);
        };

        return createPdf(type.name().replace('_', ' '), lines);
    }

    private List<String> buildSalesSummaryLines(SalesSummaryReportResponse summary) {
        List<String> lines = new ArrayList<>();
        lines.add("Sales Summary Report");
        lines.add("");
        lines.add("Total Sales: " + formatAmount(summary.getTotalSales()));
        lines.add("Total Transactions: " + summary.getTotalTransactions());
        lines.add("Average Order Value: " + formatAmount(summary.getAveragedOrderValue()));
        lines.add("");
        lines.add("Sales Details:");
        if (summary.getSales() != null) {
            summary.getSales().forEach(sale -> lines.add("Sale " + sale.getSaleId() + ": "
                    + formatAmount(sale.getTotalAmount()) + " - "
                    + sale.getPaymentMethod().name() + " - "
                    + sale.getStatus().name() + " - "
                    + sale.getCreatedAt()));
        }
        return lines;
    }

    private List<String> buildInventoryMovementLines(InventoryMovementReportResponse response) {
        List<String> lines = new ArrayList<>();
        lines.add("Inventory Movement Report");
        lines.add("");
        lines.add("Total Stock In: " + response.getTotalStockIn());
        lines.add("Total Stock Out: " + response.getTotalStockOut());
        lines.add("Total Adjustments: " + response.getTotalAdjustments());
        lines.add("");
        lines.add("Inventory Logs:");
        if (response.getLogs() != null) {
            response.getLogs().forEach(log -> lines.add("Log " + log.getInventoryLogId() + ": "
                    + log.getProduct().getName() + " - "
                    + log.getType().name() + " - "
                    + log.getQuantity() + " - "
                    + log.getReason() + " - "
                    + log.getCreatedAt()));
        }
        return lines;
    }

    private List<String> buildLowStockLines(LowStockReportResponse response) {
        List<String> lines = new ArrayList<>();
        lines.add("Low Stock Report");
        lines.add("");
        lines.add("Low Stock Count: " + response.getLowStockCount());
        lines.add("");
        lines.add("Products:");
        if (response.getLowStockProducts() != null) {
            response.getLowStockProducts().forEach(product -> lines.add(product.getProductId() + ": "
                    + product.getName() + " (" + product.getSku() + ") - stock "
                    + product.getStockQuantity() + " / min " + product.getMinimumStock()));
        }
        return lines;
    }

    private List<String> buildCategoryPerformanceLines(CategoryPerformanceReportResponse response) {
        List<String> lines = new ArrayList<>();
        lines.add("Category Performance Report");
        lines.add("");
        lines.add("Total Revenue: " + formatAmount(response.getTotalRevenue()));
        lines.add("Total Products Sold: " + response.getTotalProductSold());
        lines.add("Top Category: " + response.getTopCategory());
        lines.add("");
        lines.add("Category Breakdown:");
        if (response.getCategoryRevenue() != null) {
            response.getCategoryRevenue().forEach(metric -> lines.add(metric.getCategoryName() + " (" + metric.getCategoryId() + "): "
                    + formatAmount(metric.getRevenue()) + " sales, "
                    + metric.getProductsSold() + " units"));
        }
        return lines;
    }

    private List<String> buildSupplierPerformanceLines(SupplierPerformanceReportResponse response) {
        List<String> lines = new ArrayList<>();
        lines.add("Supplier Performance Report");
        lines.add("");
        lines.add("Total Supplier Revenue: " + formatAmount(response.getTotalSupplierRevenue()));
        lines.add("Top Supplier: " + response.getTopSupplier());
        lines.add("");
        lines.add("Supplier Contributions:");
        if (response.getSupplierContributions() != null) {
            response.getSupplierContributions().forEach(contribution -> lines.add(contribution.getSupplierName() + " (" + contribution.getSupplierId() + "): "
                    + formatAmount(contribution.getContributionAmount()) + " - "
                    + contribution.getSupplierProducts() + " products"));
        }
        return lines;
    }

    private byte[] createPdf(String title, List<String> lines) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            output.write("%PDF-1.4\n".getBytes(StandardCharsets.ISO_8859_1));

            int object1 = output.size();
            output.write("1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n".getBytes(StandardCharsets.ISO_8859_1));

            int object2 = output.size();
            output.write("2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n".getBytes(StandardCharsets.ISO_8859_1));

            int object3 = output.size();
            output.write("3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Resources << /Font << /F1 5 0 R >> >> /Contents 4 0 R >>\nendobj\n".getBytes(StandardCharsets.ISO_8859_1));

            byte[] contentBytes = buildPageContent(title, lines);
            int object4 = output.size();
            output.write(("4 0 obj\n<< /Length " + contentBytes.length + " >>\nstream\n").getBytes(StandardCharsets.ISO_8859_1));
            output.write(contentBytes);
            output.write("\nendstream\nendobj\n".getBytes(StandardCharsets.ISO_8859_1));

            int object5 = output.size();
            output.write("5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n".getBytes(StandardCharsets.ISO_8859_1));

            int xrefOffset = output.size();
            output.write("xref\n0 6\n0000000000 65535 f \n".getBytes(StandardCharsets.ISO_8859_1));
            output.write(formatOffset(object1).getBytes(StandardCharsets.ISO_8859_1));
            output.write(formatOffset(object2).getBytes(StandardCharsets.ISO_8859_1));
            output.write(formatOffset(object3).getBytes(StandardCharsets.ISO_8859_1));
            output.write(formatOffset(object4).getBytes(StandardCharsets.ISO_8859_1));
            output.write(formatOffset(object5).getBytes(StandardCharsets.ISO_8859_1));
            output.write("trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n".getBytes(StandardCharsets.ISO_8859_1));
            output.write(Integer.toString(xrefOffset).getBytes(StandardCharsets.ISO_8859_1));
            output.write("\n%%EOF".getBytes(StandardCharsets.ISO_8859_1));

            return output.toByteArray();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private byte[] buildPageContent(String title, List<String> lines) {
        StringBuilder builder = new StringBuilder();
        builder.append("BT\n/F1 12 Tf\n50 760 Td\n");
        builder.append("(").append(escapePdf(title)).append(") Tj\nT*\n");
        for (String line : lines) {
            builder.append("(").append(escapePdf(line)).append(") Tj\nT*\n");
        }
        builder.append("ET");
        return builder.toString().getBytes(StandardCharsets.ISO_8859_1);
    }

    private String escapePdf(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private String formatOffset(int offset) {
        return String.format("%010d 00000 n \n", offset);
    }

    private String formatAmount(BigDecimal amount) {
        return amount == null ? "0" : amount.toString();
    }
}
