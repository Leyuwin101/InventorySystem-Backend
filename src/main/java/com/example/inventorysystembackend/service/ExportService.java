package com.example.inventorysystembackend.service;

import com.example.inventorysystembackend.model.enums.ExportFormat;
import com.example.inventorysystembackend.model.enums.ReportType;

public interface ExportService {

    byte[] export(ReportType type, ExportFormat format);
}
