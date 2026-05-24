package com.example.inventorysystembackend.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(
                "products",
                "categories",
                "suppliers",
                "dashboard",
                "inventoryLogs",
                "sales",
                "reports"
        );

        // Short TTL keeps inventory data accurate while absorbing repeated dashboard/table reads.
        manager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(5_000)
                .expireAfterWrite(Duration.ofMinutes(5))
                .recordStats());

        return manager;
    }
}
