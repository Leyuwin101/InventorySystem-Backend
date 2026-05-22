package com.example.inventorysystembackend.config;

import com.example.inventorysystembackend.model.entity.User;
import com.example.inventorysystembackend.model.enums.Role;
import com.example.inventorysystembackend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class DataInitializerConfig {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return args -> {

            log.info("[CONFIG] Running data initializer");

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
        };
    }
}
