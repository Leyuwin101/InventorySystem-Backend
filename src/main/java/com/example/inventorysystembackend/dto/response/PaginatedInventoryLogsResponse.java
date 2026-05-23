package com.example.inventorysystembackend.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
