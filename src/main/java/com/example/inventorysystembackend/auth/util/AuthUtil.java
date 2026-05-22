package com.example.inventorysystembackend.auth.util;

import com.example.inventorysystembackend.auth.security.CustomUserDetails;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor
public class AuthUtil {

    // Retrieves the current authentication object from security context.
    private static Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Returns the currently authenticated user as CustomUserDetails.
     *
     * @throws RuntimeException if user is not authenticated or principal is invalid
     */
    public static CustomUserDetails getCurrentUser() {

        Authentication auth = getAuth();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthorized request");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserDetails user) {
            return user;
        }

        throw new RuntimeException("Invalid authentication principal");
    }

    // Returns the ID of the currently authenticated user.
    public static Long getUserId() {
        return getCurrentUser().getUserId();
    }

    // Returns the email (username) of the currently authenticated user.
    public static String getEmail() {
        return getCurrentUser().getUsername();
    }

    /**
     * Checks if the current user has ADMIN role.
     *
     * @return true if user has ROLE_ADMIN authority
     */
    public static boolean isAdmin() {
        return getCurrentUser().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
