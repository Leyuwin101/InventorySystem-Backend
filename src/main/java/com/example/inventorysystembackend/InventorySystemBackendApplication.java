package com.example.inventorysystembackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class InventorySystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventorySystemBackendApplication.class, args);
    }

}
