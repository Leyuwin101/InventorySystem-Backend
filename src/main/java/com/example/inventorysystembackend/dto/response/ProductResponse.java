package com.example.inventorysystembackend.dto.response;

import com.example.inventorysystembackend.dto.nested.CategoryDTO;
import com.example.inventorysystembackend.dto.nested.ProductSupplierDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long productId;

    private CategoryDTO category;

    private String name;

    private String sku;

    private String description;

    private BigDecimal price;

    private Integer stockQuantity;

    private Integer minimumStock;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<ProductSupplierDTO> suppliers;
}
