package com.example.inventorysystembackend.repository;

import com.example.inventorysystembackend.model.entity.Sale;
import com.example.inventorysystembackend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>,
        JpaSpecificationExecutor<Sale> {

    List<Sale> findByUser(User user);
}