package com.example.inventorysystembackend.specifications;

import com.example.inventorysystembackend.model.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

    public static Specification<Product> hasCategory(Long categoryId) {

        return (root, query, cb ) ->
                 categoryId == null ? null : cb.equal(root.get("category").get("categoryId"), categoryId);
    }

    public static Specification<Product> hasSupplier(Long supplierId) {

        return (root, query, cb) ->
                supplierId == null ? null : cb.equal(root.join("suppliers").get("supplierId"), supplierId);
    }

    public static Specification<Product> isLowStock(Integer threshold) {

        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("stockQuantity"), threshold);
    }



}
