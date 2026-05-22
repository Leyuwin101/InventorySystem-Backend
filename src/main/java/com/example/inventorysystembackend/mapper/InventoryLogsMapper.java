package com.example.inventorysystembackend.mapper;

import com.example.inventorysystembackend.dto.nested.ProductDTO;
import com.example.inventorysystembackend.dto.request.InventoryLogRequest;
import com.example.inventorysystembackend.dto.response.InventoryLogResponse;
import com.example.inventorysystembackend.model.entity.InventoryLogs;
import com.example.inventorysystembackend.model.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class InventoryLogsMapper {

    public InventoryLogs toEntity(InventoryLogRequest request, Product product) {

        InventoryLogs inventory = new InventoryLogs();

        inventory.setProduct(product);
        inventory.setType(request.getType());
        inventory.setQuantity(request.getQuantity());
        inventory.setReason(request.getReason());

        return inventory;

    }

    public InventoryLogResponse toDTO(InventoryLogs inventory) {

        ProductDTO product = new ProductDTO(
                inventory.getProduct().getProductID(),
                inventory.getProduct().getName(),
                inventory.getProduct().getSku()
        );

        return new InventoryLogResponse(
                inventory.getInventoryLogID(),
                product,
                inventory.getType(),
                inventory.getQuantity(),
                inventory.getReason(),
                inventory.getCreatedAt()
        );


    }
}
