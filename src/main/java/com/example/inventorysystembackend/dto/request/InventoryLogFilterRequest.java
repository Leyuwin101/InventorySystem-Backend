package com.example.inventorysystembackend.dto.request;

import com.example.inventorysystembackend.model.enums.InventoryType;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryLogFilterRequest {

    private LocalDate startDate;

    private LocalDate endDate;

    private Long productID;

    private String product;

    private InventoryType type;

    @Min(value = 0, message = "Page cannot be negative")
    @Builder.Default
    private Integer page = 0;

    @Min(value = 1, message = "Limit must be at least 1")
    @Builder.Default
    private Integer limit = 10;

    @Builder.Default
    private String sortBy = "createdAt";

    @Builder.Default
    private String sortDirection = "desc";

}
