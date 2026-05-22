package com.example.inventorysystembackend.service.implement;

import com.example.inventorysystembackend.dto.request.SaleItemRequest;
import com.example.inventorysystembackend.dto.request.SaleRequest;
import com.example.inventorysystembackend.dto.response.SaleResponse;
import com.example.inventorysystembackend.exception.ProductNotFoundException;
import com.example.inventorysystembackend.exception.SaleNotFoundException;
import com.example.inventorysystembackend.exception.UserNotFoundException;
import com.example.inventorysystembackend.mapper.SaleMapper;
import com.example.inventorysystembackend.model.entity.Product;
import com.example.inventorysystembackend.model.entity.Sale;
import com.example.inventorysystembackend.model.entity.SaleItems;
import com.example.inventorysystembackend.model.entity.User;
import com.example.inventorysystembackend.repository.ProductRepository;
import com.example.inventorysystembackend.repository.SaleItemsRepository;
import com.example.inventorysystembackend.repository.SaleRepository;
import com.example.inventorysystembackend.repository.UserRepository;
import com.example.inventorysystembackend.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final SaleItemsRepository saleItemsRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final SaleMapper saleMapper;

    /**
     * Creates a new sale transaction.
     *
     * Business rules:
     * - A sale must contain at least one item
     * - Each product must exist
     * - Product stock must be sufficient before sale is processed
     * - Stock is automatically deducted after successful validation
     *
     * Process:
     * - validate user exists
     * - validate sale items exist
     * - initialize sale entity
     * - process each item:
     *      - validate product exists
     *      - validate stock availability
     *      - deduct stock
     *      - compute subtotal
     * - compute total amount
     * - persist sale and items
     *
     * @param request sale creation request
     * @return created sale response
     */
    @Transactional
    @Override
    public SaleResponse createSale(SaleRequest request) {

        log.info("[SALE][CREATE] Start id={}", request.getUserId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> {
                    log.warn("[SALE][USER][NOT_FOUND] id={}", request.getUserId());
                    return new UserNotFoundException("User not found with id: " + request.getUserId());
                });

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Sales must contain items");
        }

        Sale sale = saleMapper.toEntity(request, user);

        List<SaleItems> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (SaleItemRequest itemReq : request.getItems()) {

            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> {
                        log.warn("[SALE][PRODUCT][NOT_FOUND] id={}", itemReq.getProductId());
                        return new ProductNotFoundException("Product not found with id: " + itemReq.getProductId());
                    });

            // Business rule: prevent overselling
            if (product.getStockQuantity() < itemReq.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for " + product.getName());
            }

            // Deduct stock after validation
            product.setStockQuantity(product.getStockQuantity() - itemReq.getQuantity());

            BigDecimal price = product.getPrice();
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(itemReq.getQuantity()));

            SaleItems item = new SaleItems();
            item.setSale(sale);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(price);
            item.setSubtotal(subtotal);

            items.add(item);
            total = total.add(subtotal);

        }

        sale.setItems(items);
        sale.setTotalAmount(total);

        Sale saved = saleRepository.save(sale);

        log.info("[SALE][CREATE] Success salesId={}", saved.getSalesID());

        return saleMapper.toDTO(saved);
    }

    /**
     * Retrieves a sale by its ID.
     *
     * Process:
     * - fetch sale by ID
     * - throw exception if not found
     * - map entity to response DTO
     *
     * @param saleId sale ID to retrieve
     * @return sale response DTO
     */
    @Override
    public SaleResponse getSaleById(Long saleId) {

        log.info("[SALE][GET] Start id={}", saleId);

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> {
                    log.warn("[SALE][GET][NOT_FOUND] id={}", saleId);
                    return new SaleNotFoundException("Sale not found with id: " + saleId);
                });

        log.info("[SALE][GET] Success id={}", saleId);

        return saleMapper.toDTO(sale);
    }

    /**
     * Retrieves all sales from the database.
     *
     * Process:
     * - fetch all sales
     * - map entities to response DTOs
     *
     * @return list of sale responses
     */
    @Override
    public List<SaleResponse> getAllSales() {

        log.info("[SALE][GET_ALL] Fetching all products");

        List<Sale> sales = saleRepository.findAll();

        return sales.stream()
                .map(saleMapper::toDTO)
                .toList();
    }

    /**
     * Retrieves all sales made by a specific user.
     *
     * Business rule:
     * - A user must exist before fetching sales
     *
     * Process:
     * - validate user exists
     * - retrieve all sales linked to the user
     * - map sales entities to response DTOs
     *
     * @param userId ID of the user
     * @return list of sales made by the user
     */
    @Override
    public List<SaleResponse> getSalesByUser(Long userId) {

        log.info("[SALE][GET_SALE_USER] Fetching sales by user");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.info("[SALE][GET_SALE_USER][NOT_FOUND] id={}", userId);
                    return new UserNotFoundException("User not found with id: "+ userId);
                });

        List<Sale> userSale = saleRepository.findByUser(user);

        return userSale.stream()
                .map(saleMapper::toDTO)
                .toList();

    }

    /**
     * Cancels an existing sale transaction.
     *
     * Business rules:
     * - Sale must exist before it can be canceled
     * - All sold items are reverted back to product stock
     * - Sale record is permanently deleted
     *
     * Process:
     * - validate sale exists
     * - restore product stock for each sold item
     * - delete sale record from database
     *
     * @param saleId ID of the sale to cancel
     */
    @Transactional
    @Override
    public void cancelSale(Long saleId) {

        log.info("[SALE][CANCEL] Start id={}", saleId);

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> {
                    log.warn("[SALE][CANCEL][NOT_FOUND] id={}", saleId);
                    return new SaleNotFoundException("Sale not found with id: " + saleId);
                });

        for (SaleItems item : sale.getItems()) {

            Product product = item.getProduct();

            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
        }

        saleRepository.delete(sale);

        log.info("[SALE][CANCEL] Success saleId={}", saleId);
    }
}
