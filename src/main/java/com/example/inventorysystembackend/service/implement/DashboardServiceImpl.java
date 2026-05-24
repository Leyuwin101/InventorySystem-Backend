package com.example.inventorysystembackend.service.implement;

import java.math.BigDecimal;
import java.util.Comparator;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventorysystembackend.dto.response.DashboardSummaryResponse;
import com.example.inventorysystembackend.mapper.DashboardMapper;
import com.example.inventorysystembackend.repository.InventoryLogsRepository;
import com.example.inventorysystembackend.repository.ProductRepository;
import com.example.inventorysystembackend.repository.SaleRepository;
import com.example.inventorysystembackend.repository.SupplierRepository;
import com.example.inventorysystembackend.service.DashboardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryLogsRepository inventoryLogsRepository;
    private final DashboardMapper dashboardMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "dashboard", key = "'summary'")
    public DashboardSummaryResponse getDashboardSummary() {
        log.info("[DASHBOARD][SUMMARY] Fetching dashboard summary metrics");

        BigDecimal totalSales = saleRepository.getTotalSales();
        if (totalSales == null) {
            totalSales = BigDecimal.ZERO;
        }

        long transactionCount = saleRepository.count();
        long lowStockCount = productRepository.countLowStockProducts();
        long stockMovementCount = inventoryLogsRepository.count();

        String topCategory = productRepository.getCategoryRevenue().stream()
                .max(Comparator.comparing(r -> r.getRevenue() == null ? BigDecimal.ZERO : r.getRevenue()))
                .map(r -> r.getCategoryName() == null ? "N/A" : r.getCategoryName())
                .orElse("N/A");

        String topSupplier = supplierRepository.getSupplierPerformance().stream()
                .max(Comparator.comparing(r -> r.getTotalContributions() == null ? BigDecimal.ZERO : r.getTotalContributions()))
                .map(r -> r.getSupplierName() == null ? "N/A" : r.getSupplierName())
                .orElse("N/A");

        return dashboardMapper.toResponse(
                totalSales,
                transactionCount,
                lowStockCount,
                stockMovementCount,
                topCategory,
                topSupplier
        );
    }
}
