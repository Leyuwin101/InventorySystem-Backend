package com.example.inventorysystembackend.auth.jwt;

import com.example.inventorysystembackend.auth.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil util;
    private final CustomUserDetailsService userDetailsService;



    /**
     * Intercepts incoming requests and authenticates users
     * using JWT tokens provided in the Authorization header.
     *
     * Public authentication endpoints are excluded from filtering.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract authorization header "Bearer Token"
        String authHeader = request.getHeader("Authorization");

        // Extract token and email if header is valid
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        log.debug("[JWT][DEBUG] JWT Token received");

        String email;

        try {
            email = util.extractEmail(token);
        } catch (Exception e) {
            log.warn("[JWT][WARNING] Invalid JWT Token format");
            filterChain.doFilter(request, response);
            return;
        }

        // Only authenticated if email exists and user is not yet authenticated
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            log.debug("[JWT][DEBUG] Authenticating user from JWT={}", email);

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Validate token before setting authentication
            if (util.validateToken(token, email)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.debug("[JWT][DEBUG] Security context set for user={}", email);
            } else {
                log.warn("[JWT][WARNING] JWT validation failed for user={}", email);
            }
        }

        // Continue request flow
        filterChain.doFilter(request, response);
    }

}
