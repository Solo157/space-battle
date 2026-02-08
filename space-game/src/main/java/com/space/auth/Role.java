package com.space.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class Role {

    public static final GrantedAuthority ADMIN = new SimpleGrantedAuthority("admin");
    public static final GrantedAuthority PRIVILEGED = new SimpleGrantedAuthority("privileged");
    public static final GrantedAuthority GAME_IS_ACCESSIBLE = new SimpleGrantedAuthority("gameIsAccessible");

}
