package com.space.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class AuthFilter extends OncePerRequestFilter {

    private static final String
            AUTH = "Authorization",
            BEARER = "Bearer ";

    private final JwtParser jwtParser;

    public AuthFilter(@Value("${otus.auth.public}") String publicKey) {
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(KeysParser.getPublicKeyFromString(publicKey))
                .build();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String header = request.getHeader(AUTH);
        if (header == null) {
            return true;
        }

        return !header.startsWith(BEARER);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(AUTH).substring(BEARER.length());

        Claims claims;
        try {
            claims = jwtParser.parseClaimsJws(token).getBody();
        } catch (Exception e) {
            setErrorResponse(HttpStatus.FORBIDDEN, response, e);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(new ClaimsAuthentication(claims));

        filterChain.doFilter(request, response);
    }

    @SneakyThrows
    private void setErrorResponse(HttpStatus httpStatus, HttpServletResponse response, Exception e) {
        response.setStatus(httpStatus.value());
    }

}
