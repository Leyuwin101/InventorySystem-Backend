package com.example.inventorysystembackend.service.implement;

import com.example.inventorysystembackend.dto.analytics.CategoryRevenueDTO;
import com.example.inventorysystembackend.dto.analytics.SupplierContributionDTO;
import com.example.inventorysystembackend.dto.request.ReportFilterRequest;
import com.example.inventorysystembackend.dto.response.*;
import com.example.inventorysystembackend.mapper.AnalyticsMapper;
import com.example.inventorysystembackend.mapper.report.CategoryReportMapper;
import com.example.inventorysystembackend.mapper.report.InventoryReportMapper;
import com.example.inventorysystembackend.mapper.report.SalesReportMapper;
import com.example.inventorysystembackend.mapper.report.SupplierReportMapper;
import com.example.inventorysystembackend.model.enums.InventoryType;
import com.example.inventorysystembackend.projections.CategoryRevenueProjection;
import com.example.inventorysystembackend.projections.SaleSummaryProjection;
import com.example.inventorysystembackend.projections.SupplierPerformanceProjection;
import com.example.inventorysystembackend.repository.InventoryLogsRepository;
import com.example.inventorysystembackend.repository.ProductRepository;
import com.example.inventorysystembackend.repository.SaleRepository;
import com.example.inventorysystembackend.repository.SupplierRepository;
import com.example.inventorysystembackend.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryLogsRepository inventoryLogsRepository;

    private final SalesReportMapper salesReportMapper;
    private final InventoryReportMapper inventoryReportMapper;
    private final CategoryReportMapper categoryReportMapper;
    private final SupplierReportMapper supplierReportMapper;
    private final AnalyticsMapper analyticsMapper;

    @Override
    public SalesSummaryReportResponse getSaleSummary(ReportFilterRequest request) {

        SaleSummaryProjection summary = saleRepository.getSalesSummary();

        return salesReportMapper.toResponse(
                summary.getTotalSales(),
                summary.getTotalTransactions(),
                summary.getAverageOrderValue(),
                List.of(),
                saleRepository.findAll()
        );
    }

    @Override
    public InventoryMovementReportResponse getInventoryMovement(ReportFilterRequest request) {

        long stockIn = inventoryLogsRepository.countByType(InventoryType.STOCK_IN);

        long stockOut = inventoryLogsRepository.countByType(InventoryType.STOCK_OUT);

        long adjustments = inventoryLogsRepository.countByType(InventoryType.ADJUSTMENT);

        return inventoryReportMapper.toInventoryMovementResponse(
                stockIn,
                stockOut,
                adjustments,
                List.of(),
                inventoryLogsRepository.findAll()
        );
    }

    @Override
    public LowStockReportResponse getLowStock(ReportFilterRequest request) {

        var products = productRepository.findLowStockProducts();

        return inventoryReportMapper.toLowStockReportResponse(
                (long) products.size(),
                products
        );
    }

    @Override
    public CategoryPerformanceReportResponse getCategoryPerformance(ReportFilterRequest request) {

        List<CategoryRevenueProjection> projections =
                productRepository.getCategoryRevenue();

        List<CategoryRevenueDTO> revenueDTOs = projections.stream()
                .map(projection -> analyticsMapper.toCategoryRevenueDTO(
                        projection.getCategoryId(),
                        projection.getCategoryName(),
                        projection.getRevenue(),
                        projection.getProductsSold(),
                        0.0
                ))
                .toList();

        BigDecimal totalRevenue = revenueDTOs.stream()
                .map(CategoryRevenueDTO::getRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long totalProductsSold = revenueDTOs.stream()
                .map(CategoryRevenueDTO::getProductsSold)
                .reduce(0L, Long::sum);

        String topCategory = revenueDTOs.stream()
                .max(Comparator.comparing(CategoryRevenueDTO::getRevenue))
                .map(CategoryRevenueDTO::getCategoryName)
                .orElse("N/A");

        return categoryReportMapper.toResponse(
                totalRevenue,
                totalProductsSold,
                topCategory,
                revenueDTOs
        );
    }

    @Override
    public SupplierPerformanceReportResponse getSupplierPerformance(ReportFilterRequest request) {

        List<SupplierPerformanceProjection> projections =
                supplierRepository.getSupplierPerformance();

        List<SupplierContributionDTO> contributions = projections.stream()
                .map(projection -> analyticsMapper.toSupplierContributionDTO(
                        projection.getSupplierId(),
                        projection.getSupplierName(),
                        projection.getTotalContributions(),
                        0.0,
                        projection.getSupplierProducts()
                ))
                .toList();

        BigDecimal totalSupplierRevenue = contributions.stream()
                .map(SupplierContributionDTO::getContributionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String topSupplier = contributions.stream()
                .max(Comparator.comparing(SupplierContributionDTO::getContributionAmount))
                .map(SupplierContributionDTO::getSupplierName)
                .orElse("N/A");

        return supplierReportMapper.toResponse(
                totalSupplierRevenue,
                topSupplier,
                contributions
        );
    }
}