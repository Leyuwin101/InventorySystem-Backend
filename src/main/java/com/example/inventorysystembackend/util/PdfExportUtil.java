package com.example.inventorysystembackend.util;

import java.io.OutputStream;

public class PdfExportUtil {

    // placeholder structure (you will plug iText/OpenPDF later)

    public static void generateSimpleReport(OutputStream out, String content) {
        try {
            out.write(content.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }

}
