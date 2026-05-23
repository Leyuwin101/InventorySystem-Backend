package com.example.inventorysystembackend.dto.analytics;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KPIResponseDTO {

    private String title;

    private String value;

    private String description;

    private Double percentageChange;
}
