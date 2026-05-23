package com.example.inventorysystembackend.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Value("${app.frontend-url:https://ssims-project-frontend.vercel.app}")
    private String frontendUrl;

    /**
     * Credentials are enabled because refresh tokens are stored in HttpOnly
     * cookies. Origins must stay explicit/pattern-based; browsers reject
     * credentialed requests when the API replies with a wildcard origin.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        List<String> originPatterns = new ArrayList<>(List.of(
                "http://localhost:5173",
                "https://*.vercel.app"
        ));

        if (frontendUrl != null && !frontendUrl.isBlank()) {
            originPatterns.add(frontendUrl.trim());
        }

        config.setAllowedOriginPatterns(originPatterns);

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization", "Content-Type", "Location"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}


