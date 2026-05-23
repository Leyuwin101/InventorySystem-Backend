package com.example.inventorysystembackend.specifications;

import com.example.inventorysystembackend.model.entity.Sale;
import com.example.inventorysystembackend.model.enums.PaymentMethod;
import com.example.inventorysystembackend.model.enums.SaleStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class SaleSpecifications {

    public static Specification<Sale> dateBetween(LocalDate start, LocalDate end) {

        return (root, query, cb) -> {
            if (start == null || end == null) return null;
            return cb.between(root.get("createdAt"), start.atStartOfDay(), end.atTime(23, 59, 59));
        };
    }

    public static Specification<Sale> hasStatus(SaleStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Sale> hasPaymentMethod(PaymentMethod method) {
        return (root, query, cb) ->
                method == null ? null : cb.equal(root.get("paymentMethod"), method);
    }
}
