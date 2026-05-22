package com.example.inventorysystembackend.service;

import com.example.inventorysystembackend.dto.request.ProductRequest;
import com.example.inventorysystembackend.dto.request.ProductSupplierRequest;
import com.example.inventorysystembackend.dto.response.ProductResponse;
import com.example.inventorysystembackend.model.entity.Product;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long productId, ProductRequest request);

    void deleteProduct(Long productId);

    ProductResponse getProductById(Long productId);

    List<ProductResponse> getAllProducts();

    ProductResponse updateStock(Long productId, Integer quantity);

    List<ProductResponse> getLowStockProduct();

}
