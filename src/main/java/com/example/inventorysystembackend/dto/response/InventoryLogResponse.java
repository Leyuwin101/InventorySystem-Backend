package com.example.inventorysystembackend.dto.response;

import com.example.inventorysystembackend.dto.nested.ProductDTO;
import com.example.inventorysystembackend.model.enums.InventoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryLogResponse {

    private Long inventoryLogId;

    private ProductDTO product;

    private InventoryType type;

    private Integer quantity;

    private String reason;

    private LocalDateTime createdAt;
}
