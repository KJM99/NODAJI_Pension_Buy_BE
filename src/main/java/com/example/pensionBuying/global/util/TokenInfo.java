package com.example.pensionBuying.global.util;

import io.jsonwebtoken.Claims;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record TokenInfo(
    String userId, String name, String email
) implements UserDetails {
    public static TokenInfo fromClaims(Claims claims){
        String userId = claims.get("id", String.class);
        String name = claims.get("name", String.class);
        String email = claims.get("email", String.class);
        return new TokenInfo(userId, name, email);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}