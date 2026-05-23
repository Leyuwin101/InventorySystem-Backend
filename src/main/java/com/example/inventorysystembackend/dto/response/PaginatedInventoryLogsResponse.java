package com.example.inventorysystembackend.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedInventoryLogsResponse {

    private List<InventoryLogResponse> logs;

    private Long totalItems;

    private Integer totalPages;

    private Integer page;

    private Integer limit;
 }
