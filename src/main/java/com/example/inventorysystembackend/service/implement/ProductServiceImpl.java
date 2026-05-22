package com.example.inventorysystembackend.service.implement;

import com.example.inventorysystembackend.dto.request.ProductRequest;
import com.example.inventorysystembackend.dto.response.ProductResponse;
import com.example.inventorysystembackend.exception.CategoryNotFoundException;
import com.example.inventorysystembackend.exception.DuplicateSkuException;
import com.example.inventorysystembackend.exception.ProductNotFoundException;
import com.example.inventorysystembackend.exception.SupplierNotFoundException;
import com.example.inventorysystembackend.mapper.ProductMapper;
import com.example.inventorysystembackend.model.entity.Category;
import com.example.inventorysystembackend.model.entity.Product;
import com.example.inventorysystembackend.model.entity.ProductSupplier;
import com.example.inventorysystembackend.model.entity.Supplier;
import com.example.inventorysystembackend.repository.CategoryRepository;
import com.example.inventorysystembackend.repository.ProductRepository;
import com.example.inventorysystembackend.repository.ProductSupplierRepository;
import com.example.inventorysystembackend.repository.SupplierRepository;
import com.example.inventorysystembackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final ProductSupplierRepository productSupplierRepository;
    private final SupplierRepository supplierRepository;

    /**
     * Creates a new product.
     *
     * Business rules:
     * - SKU must be unique
     * - Category must exist
     *
     * Process:
     * - validate SKU uniqueness
     * - validate category existence
     * - map request DTO → entity
     * - persist product entity
     * - return response DTO
     *
     * @param request product registration data
     * @return created product response
     */
    @Transactional
    @Override
    public ProductResponse createProduct(ProductRequest request) {

        log.info("[PRODUCT][CREATE] Start name={}. sku={}", request.getName(), request.getSku());


        if (productRepository.existsBySku(request.getSku())) {

            log.warn("[PRODUCT][CREATE][SKU_EXISTS] sku={}", request.getSku());

            throw new DuplicateSkuException("SKU already exists: " + request.getSku());
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        Product product = productMapper.toEntity(request, category);

        Product saved = productRepository.save(product);

        if(request.getSuppliers() != null && !request.getSuppliers().isEmpty()) {

            for (var supplierRequest : request.getSuppliers()) {

                Supplier supplier = supplierRepository.findById(supplierRequest.getSupplierId())
                        .orElseThrow(() -> {
                            log.warn("[SUPPLIER][CREATE][NOT_FOUND] id={}", supplierRequest.getSupplierId());
                            return new SupplierNotFoundException("Supplier not found: " + supplierRequest.getSupplierId());
                        });

                boolean exists = productSupplierRepository.existsByProductAndSupplier(saved, supplier);

                if (exists) continue;

                ProductSupplier ps = new ProductSupplier();
                ps.setProduct(saved);
                ps.setSupplier(supplier);
                ps.setSupplierPrice(supplierRequest.getSupplierPrice());
                ps.setLeadTimeDays(supplierRequest.getLeadTimeDays());

                productSupplierRepository.save(ps);
            }
        }

        log.info("[PRODUCT][CREATE] Success id={}, name={}", saved.getProductID(), saved.getName());

        return productMapper.toDTO(saved);
    }

    /**
     * Updates an existing product.
     *
     * Business rules:
     * - Product must exist
     * - Category must exist
     * - SKU must remain unique (if changed)
     *
     * Process:
     * - validate category exists
     * - fetch product by ID
     * - update basic product fields
     * - validate SKU uniqueness if changed
     * - persist updated product
     * - return response DTO
     *
     * @param productId product ID to update
     * @param request updated product data
     * @return updated product response
     */
    @Transactional
    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest request) {

        log.info("[PRODUCT][UPDATE] Start id={}", productId);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> {
                    log.warn("[PRODUCT-CATEGORY][UPDATE][NOT_FOUND] id={}", request.getCategoryId());
                    return new CategoryNotFoundException("Category not found: " + request.getCategoryId());
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("[PRODUCT][UPDATE][NOT_FOUND] id={}", productId);
                    return new ProductNotFoundException("Product not found: " + productId);
                });

        product.setCategory(category);
        product.setName(request.getName());

        if (!product.getSku().equals(request.getSku())) {

            boolean skuExists = productRepository.existsBySku(request.getSku());

            if (skuExists) {
                log.warn("[PRODUCT][UPDATE][SKU_EXISTS] sku={}", request.getSku());

                throw new DuplicateSkuException("SKU already exists: " + request.getSku());
            }

            product.setSku(request.getSku());
        }

        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setMinimumStock(request.getMinimumStock());

        Product updated = productRepository.save(product);

        log.info("[PRODUCT][UPDATE] Success id={}", productId);

        return productMapper.toDTO(updated);

    }

    /**
     * Deletes a product by ID.
     *
     * Process:
     * - verifies if product exists
     * - throws ProductNotFoundException if not found
     * - deletes product from database
     *
     * @param productId product ID to delete
     */
    @Transactional
    @Override
    public void deleteProduct(Long productId) {

        log.info("[PRODUCT][DELETE] Start id={}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("[PRODUCT][DELETE][NOT_FOUND] id={}", productId);
                    return new ProductNotFoundException("Product not found: " + productId);
                });


        productRepository.delete(product);

        log.info("[PRODUCT][DELETE] Success id={}", productId);
    }

    /**
     * Retrieves a product by its ID.
     *
     * Process:
     * - fetch product by ID
     * - throw exception if not found
     * - map entity to response DTO
     *
     * @param productId product ID to retrieve
     * @return product response DTO
     */
    @Override
    public ProductResponse getProductById(Long productId) {

        log.info("[PRODUCT][GET] Start id={}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("[PRODUCT][GET][NOT_FOUND] id={}", productId);
                    return new ProductNotFoundException("Product not found: " + productId);
                });

        log.info("[PRODUCT][GET] Success id={}", productId);

        return productMapper.toDTO(product);
    }

    /**
     * Retrieves all products from the database.
     *
     * Process:
     * - fetch all products
     * - map entities to response DTOs
     *
     * @return list of product responses
     */
    @Override
    public List<ProductResponse> getAllProducts() {

        log.info("[PRODUCT][GET_ALL] Fetching all products");

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(productMapper::toDTO)
                .toList();
    }

    /**
     * Updates product stock quantity.
     *
     * Process:
     * - fetch product by ID
     * - calculate new stock value
     * - validate stock is not negative
     * - update and save product
     * - return updated product
     *
     * @param productId product ID
     * @param quantity stock adjustment (positive or negative)
     * @return updated product response
     */
    @Transactional
    @Override
    public ProductResponse updateStock(Long productId, Integer quantity) {

        log.info("[PRODUCT][STOCKS][UPDATE] Start id={}, qty={}", productId, quantity);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("[PRODUCT][STOCK][UPDATE][NOT_FOUND] not found={}", productId);
                    return new ProductNotFoundException("Product not found: " + productId);
                });

        int newStock = product.getStockQuantity() + quantity;

        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot go below 0");
        }

        product.setStockQuantity(newStock);

        Product updated = productRepository.save(product);

        log.info("[PRODUCT][STOCK][UPDATE] Success id={}, newStock={}", productId, newStock);

        return productMapper.toDTO(updated);
    }

    /**
     * Retrieves all products that are currently below or equal to their minimum stock level.
     *
     * Business rule:
     * - A product is considered "low stock" when stockQuantity <= minimumStock
     *
     * Process:
     * - fetch low-stock products from database using optimized query
     * - map entities to response DTOs
     *
     * @return list of low-stock product responses
     */
    @Override
    public List<ProductResponse> getLowStockProduct() {

        log.info("[PRODUCTS][LOW_STOCK] Fetching low stock products");

        List<Product> products = productRepository.findLowStockProducts();

        return products.stream()
                .map(productMapper::toDTO)
                .toList();
    }


}
