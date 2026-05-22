package com.example.inventorysystembackend.repository;

import com.example.inventorysystembackend.model.entity.Product;
import com.example.inventorysystembackend.model.entity.ProductSupplier;
import com.example.inventorysystembackend.model.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSupplierRepository extends JpaRepository<ProductSupplier, Long> {

    boolean existsByProductAndSupplier(Product product, Supplier supplier);

    Optional<ProductSupplier> findByProductAndSupplier(Product product, Supplier supplier);
}
