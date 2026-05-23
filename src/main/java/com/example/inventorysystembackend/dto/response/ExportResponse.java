package com.example.inventorysystembackend.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportResponse {

    private String fileName;

    private String contentType;

    private String downloadUrl;
}
