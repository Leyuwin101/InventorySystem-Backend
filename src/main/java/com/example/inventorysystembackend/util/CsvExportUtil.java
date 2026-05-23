package com.example.inventorysystembackend.util;

import java.io.PrintWriter;

public class CsvExportUtil {

    public static void writeHeader(PrintWriter writer, String... headers) {
        writer.println(String.join(",", headers));
    }

    public static void writeRow(PrintWriter writer, Object... values) {

        StringBuilder row = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            row.append(values[i]);
            if(i < values.length - 1) row.append(",");
        }

        writer.println(row);
    }
}
