package com.example.inventorysystembackend.service;

import com.example.inventorysystembackend.dto.request.SupplierRequest;
import com.example.inventorysystembackend.dto.response.SupplierResponse;
import com.example.inventorysystembackend.model.entity.Supplier;

import java.util.List;

public interface SupplierService {

    SupplierResponse createSupplier(SupplierRequest request);

    SupplierResponse updateSupplier(Long supplierId, SupplierRequest request);

    void deleteSupplier(Long supplierId);

    SupplierResponse getSupplierById(Long supplierId);

    List<SupplierResponse> getAllSuppliers();
}
