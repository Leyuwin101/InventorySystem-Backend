package com.example.inventorysystembackend.dto.response;

import com.example.inventorysystembackend.dto.analytics.KPIResponseDTO;
import lombok.*;

import java.util.List;

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
