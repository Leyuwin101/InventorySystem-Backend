package com.example.inventorysystembackend.config;

import com.example.inventorysystembackend.auth.jwt.JwtFilter;
import com.example.inventorysystembackend.auth.jwt.JwtUtil;
import com.example.inventorysystembackend.auth.service.CustomUserDetailsService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    @PostConstruct
    public void init() {
        log.info("[CONFIG] SecurityConfig initialized");
    }

    /**
     * Configures the application's HTTP security rules and JWT authentication flow.
     *
     * Security configuration:
     * - disables CSRF for stateless APIs
     * - enables CORS support
     * - uses stateless session management
     * - defines public and protected endpoints
     * - registers JWT authentication filter
     *
     * @param http Spring Security HTTP configuration
     * @param jwtFilter custom JWT authentication filter
     * @return configured security filter chain
     * @throws Exception if security configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter,  CorsConfigurationSource corsConfigurationSource) throws Exception {

        log.info("[CONFIG] Building Security Filter");

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // PUBLIC ROUTES
                        .requestMatchers(
                                "/",
                                "/api/auth/**",

                                "/v3/api-docs",
                                "/v3/api-docs/**",

                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // PROTECTED ROUTES (grouped instead of repeating)
                        .requestMatchers("/api/**").authenticated()

                        .anyRequest().authenticated()

                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\":\"Unauthorized\"}");
                        })
                )
                // Register JWT filter before Spring authentication filter
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        log.info("[CONFIG] JWT Filter registered successfully");

        return http.build();

    }

    @Bean
    public JwtFilter jwtFilter(JwtUtil jwtUtil, CustomUserDetailsService service) {

        log.info("[CONFIG] JwtFilter bean created");

        return new JwtFilter(jwtUtil, service);
    }


    /**
     * Registers BCrypt password encoder used for password hashing.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        log.info("[CONFIG] BCryptPasswordEncoder initialized");
        return new BCryptPasswordEncoder();
    }
}
