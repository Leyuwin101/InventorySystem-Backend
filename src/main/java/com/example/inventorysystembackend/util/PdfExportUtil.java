package com.example.inventorysystembackend.util;


import com.example.inventorysystembackend.exception.ExportFailedException;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PdfExportUtil {

    /**
     * Simple PDF generator placeholder.
     * Replace later with iText/OpenPDF for real formatting.
     */
    public static byte[] generateSimplePdf(String title, String content) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            StringBuilder pdfContent = new StringBuilder();

            pdfContent.append("===== ").append(title).append(" =====\n\n");
            pdfContent.append(content);

            out.write(pdfContent.toString().getBytes(StandardCharsets.UTF_8));

            return out.toByteArray();

        } catch (Exception e) {
            throw new ExportFailedException("Failed to generate PDF export: " + e.getMessage());
        }
    }

    /**
     * Direct stream writing (for API response)
     */
    public static void writeToStream(OutputStream outputStream, String title, String content) {

        try {
            StringBuilder pdfContent = new StringBuilder();

            pdfContent.append("===== ").append(title).append(" =====\n\n");
            pdfContent.append(content);

            outputStream.write(pdfContent.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();

        } catch (Exception e) {
            throw new ExportFailedException("Failed to write PDF stream: " + e.getMessage());
        }
    }


}
