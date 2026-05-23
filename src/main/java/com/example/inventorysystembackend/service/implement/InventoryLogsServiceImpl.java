package com.example.inventorysystembackend.service.implement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventorysystembackend.dto.request.InventoryLogFilterRequest;
import com.example.inventorysystembackend.dto.response.InventoryLogResponse;
import com.example.inventorysystembackend.dto.response.PaginatedInventoryLogsResponse;
import com.example.inventorysystembackend.exception.ProductNotFoundException;
import com.example.inventorysystembackend.mapper.InventoryLogsMapper;
import com.example.inventorysystembackend.model.entity.InventoryLogs;
import com.example.inventorysystembackend.model.entity.Product;
import com.example.inventorysystembackend.model.enums.InventoryType;
import com.example.inventorysystembackend.repository.InventoryLogsRepository;
import com.example.inventorysystembackend.repository.ProductRepository;
import com.example.inventorysystembackend.service.InventoryLogsService;
import com.example.inventorysystembackend.specifications.InventoryLogSpecifications;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryLogsServiceImpl implements InventoryLogsService {

    private final InventoryLogsRepository inventoryLogsRepository;
    private final ProductRepository productRepository;
    private final InventoryLogsMapper inventoryLogsMapper;

    @Override
    public PaginatedInventoryLogsResponse getLogs(InventoryLogFilterRequest request) {
        Sort.Direction direction = "asc".equalsIgnoreCase(request.getSortDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(
                Math.max(0, request.getPage()),
                Math.max(1, request.getLimit()),
                Sort.by(direction, request.getSortBy())
        );

        Specification<InventoryLogs> specification = Specification
                .where(InventoryLogSpecifications.hasProductId(request.getProductID()))
                .and(InventoryLogSpecifications.hasType(request.getType()))
                .and(InventoryLogSpecifications.createdBetween(request.getStartDate(), request.getEndDate()));

        Page<InventoryLogs> page = inventoryLogsRepository.findAll(specification, pageRequest);

        return PaginatedInventoryLogsResponse.builder()
                .logs(page.stream().map(inventoryLogsMapper::toDTO).toList())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .limit(page.getSize())
                .build();
    }

    @Transactional
    @Override
    public InventoryLogResponse createStockIn(Long productId, Integer quantity, String reason) {
        return createInventoryLog(productId, quantity, reason, InventoryType.STOCK_IN);
    }

    @Transactional
    @Override
    public InventoryLogResponse createStockOut(Long productId, Integer quantity, String reason) {
        return createInventoryLog(productId, quantity, reason, InventoryType.STOCK_OUT);
    }

    @Transactional
    @Override
    public InventoryLogResponse adjustStock(Long productId, Integer quantity, String reason) {
        return createInventoryLog(productId, quantity, reason, InventoryType.ADJUSTMENT);
    }

    private InventoryLogResponse createInventoryLog(Long productId, Integer quantity, String reason, InventoryType type) {
        log.info("[INVENTORY][LOG][CREATE] productId={}, qty={}, type={}", productId, quantity, type);

        if (quantity == null || quantity == 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

        Integer currentStock = product.getStockQuantity();
        int safeStock = currentStock == null ? 0 : currentStock;
        int updatedStock = switch (type) {
            case STOCK_IN -> safeStock + quantity;
            case STOCK_OUT -> safeStock - quantity;
            default -> safeStock + quantity;
        };

        if (updatedStock < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        product.setStockQuantity(updatedStock);
        productRepository.save(product);

        InventoryLogs inventoryLogs = new InventoryLogs();
        inventoryLogs.setProduct(product);
        inventoryLogs.setType(type);
        inventoryLogs.setQuantity(quantity);
        inventoryLogs.setReason(reason);

        InventoryLogs savedInventoryLog = inventoryLogsRepository.save(inventoryLogs);

        log.info("[INVENTORY][LOG][CREATED] id={}, newStock={}", savedInventoryLog.getInventoryLogID(), updatedStock);

        return inventoryLogsMapper.toDTO(savedInventoryLog);
    }
}
