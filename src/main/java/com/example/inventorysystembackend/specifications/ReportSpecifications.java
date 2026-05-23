package com.example.inventorysystembackend.specifications;

import org.springframework.data.jpa.domain.Specification;

public class ReportSpecifications {

    public static <T> Specification<T> combine(Specification<T>... specs) {

        Specification<T> result = Specification.where((Specification<T>) null);

        for (Specification<T> spec : specs) {
            result = result.and(spec);
        }

        return result;
    }
}
