package com.example.inventorysystembackend.repository;

import com.example.inventorysystembackend.model.entity.Product;
import com.example.inventorysystembackend.projections.CategoryRevenueProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    @Query("""
        SELECT p FROM Product p
        WHERE p.stockQuantity <= p.minimumStock
    """)
    List<Product> findLowStockProducts();

    @Query("""
        SELECT
            c.categoryID AS categoryId,
            c.name AS categoryName,
            COALESCE(SUM(si.quantity * si.price), 0) AS revenue,
            COALESCE(SUM(si.quantity), 0) AS productsSold
        FROM SaleItems si
        JOIN si.product p
        JOIN p.category c
        GROUP BY c.categoryID, c.name
    """)
    List<CategoryRevenueProjection> getCategoryRevenue();
}