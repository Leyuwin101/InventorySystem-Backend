package com.example.inventorysystembackend.repository;

import com.example.inventorysystembackend.model.entity.InventoryLogs;
import com.example.inventorysystembackend.model.entity.Product;
import com.example.inventorysystembackend.model.enums.InventoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryLogsRepository extends JpaRepository<InventoryLogs, Long>,
        JpaSpecificationExecutor<InventoryLogs> {

    List<InventoryLogs> findByProduct(Product product);

    long countByType(InventoryType type);
}