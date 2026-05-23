package com.example.inventorysystembackend.dto.request;

import com.example.inventorysystembackend.model.enums.InventoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryLogRequest {

    @NotNull(message = "Product Id is required")
    private Long productId;

    @NotNull(message = "Inventory Type required")
    private InventoryType type;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    private String reason;

}
