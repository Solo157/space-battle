package com.space.auth;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

@Data
@AllArgsConstructor
public class ClaimsAuthentication implements Authentication {

    private Claims claims;
    private List<GrantedAuthority> authorities;
    private boolean authenticated = true; // по умолчанию

    public ClaimsAuthentication(Claims claims) {
        this.claims = claims;
        this.authorities = new ArrayList<>();
    }

    public ClaimsAuthentication() {
        this.claims = null;
        this.authorities = new ArrayList<>();
        this.authenticated = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities != null && !authorities.isEmpty()) {
            return authorities;
        }

        if (claims != null) {
            Boolean isAdmin = (Boolean) claims.get("admin");
            if (Boolean.TRUE.equals(isAdmin)) {
                authorities.add(Role.ADMIN);
            }

            Boolean isPrivileged = (Boolean) claims.get("privileged");
            if (Boolean.TRUE.equals(isPrivileged)) {
                authorities.add(Role.PRIVILEGED);
            }

            Boolean gameIsAccessible = (Boolean) claims.get("gameIsAccessible");
            if (gameIsAccessible != null && gameIsAccessible) {
                authorities.add(Role.GAME_IS_ACCESSIBLE);
            }
        }
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        // Можно вернуть Subject или имя пользователя
        return claims != null ? claims.getSubject() : null;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        // Например, имя субъекта
        return claims != null ? claims.getSubject() : "Unknown";
    }
}

