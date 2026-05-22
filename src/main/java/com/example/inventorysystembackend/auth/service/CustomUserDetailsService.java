package com.example.inventorysystembackend.auth.service;

import com.example.inventorysystembackend.auth.security.CustomUserDetails;
import com.example.inventorysystembackend.model.entity.User;
import com.example.inventorysystembackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.info("[CUSTOM][SERVICE] Loading user={}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("[CUSTOM][SERVICE][NOT_FOUND] email={}", email);
                    return new UsernameNotFoundException("User not found");
                });

        return new CustomUserDetails(user);

    }
}
