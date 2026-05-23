package com.example.inventorysystembackend.dto.analytics;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendSeriesDTO {

    private String name;

    private List<ChartPointDTO> data;
}
