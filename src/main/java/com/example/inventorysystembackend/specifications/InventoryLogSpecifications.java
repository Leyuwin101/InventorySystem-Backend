package com.example.inventorysystembackend.specifications;

import com.example.inventorysystembackend.model.entity.InventoryLogs;
import com.example.inventorysystembackend.model.enums.InventoryType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class InventoryLogSpecifications {

    public static Specification<InventoryLogs> hasProductId(Long productId) {

        return (root, query, cb ) ->
                productId == null ? null : cb.equal(root.get("product").get("productId"), productId);
    }


    public static Specification<InventoryLogs> hasType(InventoryType type) {

        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<InventoryLogs> createdBetween(LocalDate start, LocalDate end) {

        return (root, query, cb) -> {
            if (start == null || end == null) return null;
            return cb.between(root.get("createdAt"), start.atStartOfDay(), end.atTime(23, 59, 59));
        } ;
    }
}
