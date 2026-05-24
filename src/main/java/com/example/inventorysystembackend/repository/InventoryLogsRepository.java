package com.example.inventorysystembackend.repository;

import com.example.inventorysystembackend.model.entity.InventoryLogs;
import com.example.inventorysystembackend.model.entity.Product;
import com.example.inventorysystembackend.model.enums.InventoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryLogsRepository extends JpaRepository<InventoryLogs, Long>,
        JpaSpecificationExecutor<InventoryLogs> {

    List<InventoryLogs> findByProduct(Product product);

    long countByType(InventoryType type);

    @Override
    @EntityGraph(attributePaths = {"product", "product.category"})
    Page<InventoryLogs> findAll(Specification<InventoryLogs> specification, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"product", "product.category"})
    List<InventoryLogs> findAll();
}
