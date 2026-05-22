package com.example.inventorysystembackend.repository;

import com.example.inventorysystembackend.model.entity.Sale;
import com.example.inventorysystembackend.model.entity.SaleItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleItemsRepository extends JpaRepository<SaleItems, Long> {

    List<SaleItems> findBySale(Sale sale);
}
