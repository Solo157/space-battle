package com.space.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/calculate/**", "/createGame/**", "/runCommand/**").permitAll() // путь без защиты
                        .anyRequest().authenticated() // все остальные требуют авторизации
                );
        return http.build();
    }
}