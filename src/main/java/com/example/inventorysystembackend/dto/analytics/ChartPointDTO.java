package com.example.inventorysystembackend.dto.analytics;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartPointDTO {

    private String label;

    private Number value;
}
