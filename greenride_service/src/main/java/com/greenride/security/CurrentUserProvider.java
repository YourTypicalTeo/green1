package com.greenride.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CurrentUserProvider {

    public Optional<CurrentUser> getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return Optional.of(new CurrentUser(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail()));
        }
        return Optional.empty();
    }
}
