package com.example.inventorysystembackend.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsSummaryResponse {

    private List<KPIResponseDTO> kpis;

    private Object chartData;

    private Object tableData;
 }
