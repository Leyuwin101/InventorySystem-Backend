package com.example.inventorysystembackend.service;

import com.example.inventorysystembackend.dto.request.ProductSupplierRequest;
import com.example.inventorysystembackend.dto.response.ProductResponse;

public interface ProductSupplierService {

    ProductResponse assignSupplier(Long productId, ProductSupplierRequest request);

    ProductResponse removeSupplier(Long productId, Long supplierId);
}
