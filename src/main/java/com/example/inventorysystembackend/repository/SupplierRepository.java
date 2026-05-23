package com.example.inventorysystembackend.repository;

import com.example.inventorysystembackend.model.entity.Supplier;
import com.example.inventorysystembackend.projections.SupplierPerformanceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>,
        JpaSpecificationExecutor<Supplier> {

    Optional<Supplier> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
        SELECT
            s.supplierID AS supplierId,
            s.name AS supplierName,
            COALESCE(SUM(ps.supplierPrice), 0) AS totalContribution,
            COUNT(ps.productSupplierID) AS suppliedProducts
        FROM Supplier s
        JOIN s.productSuppliers ps
        GROUP BY s.supplierID, s.name
    """)
    List<SupplierPerformanceProjection> getSupplierPerformance();
}