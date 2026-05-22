package com.example.inventorysystembackend.service.implement;

import com.example.inventorysystembackend.dto.request.ProductSupplierRequest;
import com.example.inventorysystembackend.dto.response.ProductResponse;
import com.example.inventorysystembackend.exception.ProductNotFoundException;
import com.example.inventorysystembackend.exception.SupplierNotFoundException;
import com.example.inventorysystembackend.mapper.ProductMapper;
import com.example.inventorysystembackend.model.entity.Product;
import com.example.inventorysystembackend.model.entity.ProductSupplier;
import com.example.inventorysystembackend.model.entity.Supplier;
import com.example.inventorysystembackend.repository.ProductRepository;
import com.example.inventorysystembackend.repository.ProductSupplierRepository;
import com.example.inventorysystembackend.repository.SupplierRepository;
import com.example.inventorysystembackend.service.ProductSupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSupplierServiceImpl implements ProductSupplierService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ProductSupplierRepository productSupplierRepository;
    private final ProductMapper productMapper;

    /**
     * Assigns or updates a supplier relationship for a product.
     *
     * Business behavior:
     * - If a relationship already exists → update supplier details
     * - If no relationship exists → create a new one
     *
     * Process:
     * - validate product exists
     * - validate supplier exists
     * - validate supplier data (price, lead time)
     * - fetch existing relationship OR create new one
     * - apply changes only if needed
     * - persist changes if modified
     *
     * @param productId product ID
     * @param request supplier assignment data
     * @return updated product response
     */
    @Transactional
    @Override
    public ProductResponse assignSupplier(Long productId, ProductSupplierRequest request) {

        log.info("[PRODUCT][SUPPLIER][ASSIGN] productId={}, supplierId={}", productId, request.getSupplierId());

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("[PRODUCT][ASSIGN][NOT_FOUND] not found={}", productId);
                    return new ProductNotFoundException("Product not found: " + productId);
                });

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> {
                    log.warn("[SUPPLIER][ASSIGN][NOT_FOUND] id={}", request.getSupplierId());
                    return new SupplierNotFoundException("Supplier not found: " + request.getSupplierId()   );
                });

        validateSupplierData(request);

        ProductSupplier relation = productSupplierRepository
                .findByProductAndSupplier(product, supplier)
                .orElseGet(() -> {
                    log.info("[PRODUCT_SUPPLIER][CREATE] New relation productId={}, supplierId={}",
                            productId, request.getSupplierId());

                    ProductSupplier ps = new ProductSupplier();
                    ps.setProduct(product);
                    ps.setSupplier(supplier);
                    return ps;
                });

        boolean updated = applyChanges(relation, request);

        if (updated) {
            productSupplierRepository.save(relation);
            log.info("[PRODUCT_SUPPLIER][SAVE] Changes persisted");
        } else {
            log.info("[PRODUCT_SUPPLIER][NO_CHANGE] Skipped save");
        }

        return productMapper.toDTO(product);
    }

    /**
     * Removes a supplier from a product.
     *
     * Business rule:
     * - A product-supplier relationship must exist before deletion
     *
     * Process:
     * - validate product exists
     * - validate supplier exists
     * - locate existing relationship
     * - delete relationship
     *
     * @param productId product ID
     * @param supplierId supplier ID
     * @return updated product response
     */
    @Transactional
    @Override
    public ProductResponse removeSupplier(Long productId, Long supplierId) {

        log.info("[PRODUCT][SUPPLIER][REMOVE] productId={}, supplierId={}", productId, supplierId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("[PRODUCT][REMOVE][NOT_FOUND] not found={}", productId);
                    return new ProductNotFoundException("Product not found: " + productId);
                });

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> {
                    log.warn("[SUPPLIER][REMOVE][NOT_FOUND] id={}", supplierId);
                    return new SupplierNotFoundException("Supplier not found: " + supplierId);
                });

        ProductSupplier ps = productSupplierRepository
                .findByProductAndSupplier(product, supplier)
                .orElseThrow(() -> new RuntimeException("Relation not found"));

        productSupplierRepository.delete(ps);

        log.info("[PRODUCT][SUPPLIER][REMOVE] Success");

        return productMapper.toDTO(product);
    }


    /**
     * Validates supplier assignment input data.
     *
     * Rules:
     * - supplierPrice must not be negative
     * - leadTimeDays must not be negative
     *
     * @param request supplier assignment data
     */
    private void validateSupplierData(ProductSupplierRequest request) {

        if (request.getSupplierPrice() != null && request.getSupplierPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Supplier price cannot be negative ");
        }

        if (request.getLeadTimeDays() != null && request.getLeadTimeDays() < 0) {
            throw new IllegalArgumentException("Lead time cannot be negative");
        }
    }

    /**
     * Applies updates to an existing product-supplier relationship.
     *
     * Behavior:
     * - Only updates fields that changed
     * - Returns true if any modification occurred
     *
     * @param relation existing entity
     * @param request update data
     * @return true if changes were applied
     */
    private boolean applyChanges(ProductSupplier relation, ProductSupplierRequest request) {

        boolean changed = false;

        if (request.getSupplierPrice() != null &&
                !request.getSupplierPrice().equals(relation.getSupplierPrice())) {

            relation.setSupplierPrice(request.getSupplierPrice());
            changed = true;
        }

        if (request.getLeadTimeDays() != null &&
                !request.getLeadTimeDays().equals(relation.getLeadTimeDays())) {

            relation.setLeadTimeDays(request.getLeadTimeDays());
            changed = true;
        }

        return changed;
    }

}
