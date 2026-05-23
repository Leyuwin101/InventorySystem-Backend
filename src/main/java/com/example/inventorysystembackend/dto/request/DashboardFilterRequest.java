package com.example.inventorysystembackend.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashboardFilterRequest {

    private LocalDate startDate;

    private LocalDate endDate;

    private Long categoryId;

    private Long supplierId;

    private Long productId;
}
