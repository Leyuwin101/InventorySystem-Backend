package com.example.inventorysystembackend.mapper;

import com.example.inventorysystembackend.dto.nested.CategoryDTO;
import com.example.inventorysystembackend.dto.nested.ProductSupplierDTO;
import com.example.inventorysystembackend.dto.request.ProductRequest;
import com.example.inventorysystembackend.dto.response.ProductResponse;
import com.example.inventorysystembackend.model.entity.Category;
import com.example.inventorysystembackend.model.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request, Category category) {

        Product product = new Product();

        product.setCategory(category);
        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setMinimumStock(request.getMinimumStock());

        return product;
    }

    public ProductResponse toDTO(Product product) {

        CategoryDTO category = null;

        if (product.getCategory() != null) {

            category = new CategoryDTO(
                    product.getCategory().getCategoryID(),
                    product.getCategory().getName()
            );
        }

        List<ProductSupplierDTO> suppliers =
                product.getSuppliers() == null
                        ? List.of()
                        : product.getSuppliers()
                        .stream()
                        .map(ps -> new ProductSupplierDTO(
                                ps.getSupplier().getSupplierID(),
                                ps.getSupplier().getName(),
                                ps.getSupplier().getCompanyName(),
                                ps.getSupplierPrice(),
                                ps.getLeadTimeDays()
                        ))
                        .toList();

        return new ProductResponse(
                product.getProductID(),
                category,
                product.getName(),
                product.getSku(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getMinimumStock(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                suppliers
        );
    }
}
