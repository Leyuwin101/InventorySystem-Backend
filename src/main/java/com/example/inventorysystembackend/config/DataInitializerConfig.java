package com.example.inventorysystembackend.config;

import com.example.inventorysystembackend.model.entity.User;
import com.example.inventorysystembackend.model.enums.Role;
import com.example.inventorysystembackend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class DataInitializerConfig {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder, JdbcTemplate jdbcTemplate) {

        return args -> {

            log.info("[CONFIG] Running data initializer");

            jdbcTemplate.execute("ALTER TABLE users DROP CONSTRAINT IF EXISTS users_role_check");
            jdbcTemplate.execute("""
                    ALTER TABLE users
                    ADD CONSTRAINT users_role_check
                    CHECK (role IN ('ADMIN', 'MANAGER', 'CASHIER', 'INVENTORY_CLERK', 'GUEST'))
                    """);

            if(userRepository.findByEmail("admin@gmail.com").isEmpty()) {

                log.info("[CONFIG] Creating default admin user");

                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                log.info("[CONFIG] Default admin created" );
            } else {
                log.info("[CONFIG] Admin already exists, skipping");
            }

            if(userRepository.findByUsername("Guest").isEmpty()) {

                log.info("[CONFIG] Creating default guest user");

                User guest = new User();
                guest.setUsername("Guest");
                guest.setEmail("guest@saristore.local");
                guest.setPassword(passwordEncoder.encode("Guest"));
                guest.setRole(Role.GUEST);

                userRepository.save(guest);

                log.info("[CONFIG] Default guest created" );
            } else {
                log.info("[CONFIG] Guest already exists, skipping");
            }
        };
    }
}
