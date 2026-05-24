package com.example.inventorysystembackend.service.implement;

import com.example.inventorysystembackend.dto.request.SupplierRequest;
import com.example.inventorysystembackend.dto.response.SupplierResponse;
import com.example.inventorysystembackend.exception.EmailAlreadyExistException;
import com.example.inventorysystembackend.exception.SupplierNotFoundException;
import com.example.inventorysystembackend.mapper.SupplierMapper;
import com.example.inventorysystembackend.model.entity.Supplier;
import com.example.inventorysystembackend.repository.SupplierRepository;
import com.example.inventorysystembackend.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    /**
     * Create Supplier
     *
     * Process:
     * - Maps the request DTO to supplier Entity
     * - Saves the supplier to the database
     *
     * @param request supplier registration data
     * @return saved supplier response
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "suppliers", allEntries = true),
            @CacheEvict(cacheNames = "dashboard", allEntries = true),
            @CacheEvict(cacheNames = "reports", allEntries = true)
    })
    public SupplierResponse createSupplier(SupplierRequest request) {

        log.info("[SUPPLIER][CREATE] Start name={}", request.getName());

        Supplier supplier = supplierMapper.toEntity(request);

        Supplier saved = supplierRepository.save(supplier);

        log.info("[SUPPLIER][CREATE] Success id={} name={}", saved.getSupplierID(), saved.getName());

        return supplierMapper.toDTO(saved);
    }

    /**
     * Update existing supplier
     *
     * Process:
     * - Retrieves the supplier by id
     * - Throws SupplierNotFoundException if not found
     * - Validates email uniqueness before updating
     * - Updates editable supplier fields
     * - Saved the updated supplier
     *
     * @param supplierId ID of the supplier to update
     * @param request updated supplier data
     * @return updated supplier response
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "suppliers", allEntries = true),
            @CacheEvict(cacheNames = "products", allEntries = true),
            @CacheEvict(cacheNames = "dashboard", allEntries = true),
            @CacheEvict(cacheNames = "reports", allEntries = true)
    })
    public SupplierResponse updateSupplier(Long supplierId, SupplierRequest request) {

        log.info("[SUPPLIER][UPDATE] Start id={}", supplierId);

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> {
                    log.warn("[SUPPLIER][UPDATE][NOT_FOUND] id={}", supplierId);
                    return new SupplierNotFoundException("Supplier not found: " + supplierId);
                });

        supplier.setName(request.getName());

        if (!supplier.getEmail().equals(request.getEmail())) {

            boolean emailExists = supplierRepository.existsByEmail(request.getEmail());

            if (emailExists) {

                log.warn("[USER][UPDATE][EMAIL_EXISTS] email={}", request.getEmail());
                throw new EmailAlreadyExistException("Email already exists: " + request.getEmail());
            }

            supplier.setEmail(request.getEmail());

        }

        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());
        supplier.setCompanyName(request.getCompanyName());

        Supplier updated = supplierRepository.save(supplier);

        log.info("[SUPPLIER][UPDATE] Success id={}", supplierId);

        return supplierMapper.toDTO(updated);
    }

    /**
     * Delete supplier
     *
     * Process:
     * - Validates first if the supplier id exists
     * - Throw SupplierNotFoundException if not
     * - Delete supplier
     *
     * @param supplierId id of the supplier to delete
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "suppliers", allEntries = true),
            @CacheEvict(cacheNames = "products", allEntries = true),
            @CacheEvict(cacheNames = "dashboard", allEntries = true),
            @CacheEvict(cacheNames = "reports", allEntries = true)
    })
    public void deleteSupplier(Long supplierId) {

        log.info("[SUPPLIER][DELETE] Start id={}", supplierId);

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> {
                    log.warn("[SUPPLIER][DELETE][NOT_FOUND] id={}", supplierId);
                    return new SupplierNotFoundException("User not found: " + supplierId);
                });

        supplierRepository.delete(supplier);

        log.info("[SUPPLIER][DELETE] Success id={}", supplierId);
    }

    /**
     * Get supplier by id
     *
     * Process:
     * - Retrieves the supplier by id
     * - Throws SupplierNotFoundException if not found
     *
     * @param supplierId id of the supplier to get
     * @return supplier response
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "suppliers", key = "'detail:' + #supplierId")
    public SupplierResponse getSupplierById(Long supplierId) {

        log.info("[SUPPLIER][GET] Start id={}", supplierId);

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> {
                    log.warn("[SUPPLIER][GET][NOT_FOUND] id={}", supplierId);
                    return new SupplierNotFoundException("Supplier not found: " + supplierId);
                });

        log.info("[SUPPLIER][GET] Success id={}", supplierId);

        return supplierMapper.toDTO(supplier);
    }

    /**
     * Get all suppliers
     *
     * Process:
     * - Retrieve all the suppliers
     *
     * @return suppliers response
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "suppliers", key = "'all'")
    public List<SupplierResponse> getAllSuppliers() {

        log.info("[SUPPLIER][GET_ALL] Fetching all suppliers");

        List<Supplier> suppliers = supplierRepository.findAll();

        return suppliers.stream()
                .map(supplierMapper::toDTO)
                .toList();
    }

}
