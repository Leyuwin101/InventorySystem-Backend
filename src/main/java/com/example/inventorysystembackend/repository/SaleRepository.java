package com.example.inventorysystembackend.repository;

import com.example.inventorysystembackend.model.entity.Sale;
import com.example.inventorysystembackend.model.entity.User;
import com.example.inventorysystembackend.projections.SaleSummaryProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>,
        JpaSpecificationExecutor<Sale> {

    @EntityGraph(attributePaths = {"user", "items", "items.product"})
    List<Sale> findByUser(User user);

    @EntityGraph(attributePaths = {"user", "items", "items.product"})
    @Query("SELECT DISTINCT s FROM Sale s")
    List<Sale> findAllWithRelations();

    @EntityGraph(attributePaths = {"user", "items", "items.product"})
    @Query("SELECT s FROM Sale s WHERE s.salesID = :saleId")
    java.util.Optional<Sale> findDetailedById(@Param("saleId") Long saleId);

    @Query("""
        SELECT
            COALESCE(SUM(s.totalAmount), 0) AS totalSales,
            COUNT(s.salesID) AS totalTransactions,
            COALESCE(AVG(s.totalAmount), 0) AS averageOrderValue
        FROM Sale s
    """)
    SaleSummaryProjection getSalesSummary();

    @Query("""
        SELECT COALESCE(SUM(s.totalAmount), 0)
        FROM Sale s
    """)
    BigDecimal getTotalSales();
}
