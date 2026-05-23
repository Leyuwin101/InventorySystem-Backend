package com.example.inventorysystembackend.dto.analytics;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockMovementTrendDTO {

    private String date;

    private Long stockIn;

    private Long stockOut;

    private Long adjustments;

}
