package com.example.inventorysystembackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class CsvConfig {

    public static final String CSV_MEDIA_TYPE = "text/csv";

    @Bean
    public String csvEncoding() {
        return StandardCharsets.UTF_8.name();
    }
}
