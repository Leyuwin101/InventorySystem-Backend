package com.example.inventorysystembackend.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestFilterRequest {

    private LocalDate startDate;

    private LocalDate endDate;

    private Long categoryId;

    private Long productId;

    private Long supplierId;

    @Min(value = 0, message = "Page cannot be negative")
    @Builder.Default
    private Integer page = 0;

    @Min(value = 1, message = "Limit must be atleast 1")
    @Builder.Default
    private Integer limit = 10;

    @Builder.Default
    private String sortBy = "createdAt";

    @Builder.Default
    private String sortDirection = "desc";
}
