package org.auth;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.util.*;

import static org.auth.KeysParser.getPrivateKeyFromString;

@Service
public class JwtCreater {

    private final RSAPrivateKey privateKey;

    public JwtCreater(@Value("${otus.auth.private}") String privateKey) {
        this.privateKey = getPrivateKeyFromString(privateKey);
    }

    /**
     * Создание токена для пользователя, которые будет играть в определенную игру gameId.
     */
    public String createJwtForSpaceBattle(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .addClaims(Map.of(
                        "admin", user.isAdmin(),
                        "privileged", user.isPrivilegedUser(),
                        "gameIsAccessible", true
                ))
                .signWith(privateKey)
                .compact();

    }

    /**
     * Создание обычного токена для авторизации на игровом сервере. Нужен, например, для создания игры.
     */
    public String createJwt(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .addClaims(Map.of(
                        "admin", user.isAdmin(),
                        "privileged", user.isPrivilegedUser()
                ))
                .signWith(privateKey)
                .compact();

    }

}
