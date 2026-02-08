package com.space.api;

import com.space.api.dto.CommandDTO;
import com.space.auth.Role;
import com.space.entity.UObject;
import com.space.entity.User;
import com.space.service.SpaceBattleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class CommandApiController {

    private final SpaceBattleService spaceBattleService;

    private static final List<User> users = List.of(
            new User(1, "vasya", true, "123", true), // только он может создавать игры
            new User(2, "petya", false, "123", true),
            new User(3, "stepa", false, "123", false)
    );

    // key - id игры, value - список участников игры
    private static final Map<String, List<Integer>> usersOfGame = new HashMap<>();

    /**
     * Выполнить команду. Возвращаем информацию об измененном объекте.
     */
    @GetMapping("/runCommand")
    UObject runCommand(@RequestBody CommandDTO commandDTO, Authentication authentication) {
        String gameId = commandDTO.getGameId();
        if (!authentication.getAuthorities().contains(Role.GAME_IS_ACCESSIBLE)) {
            throw new AuthError("нет доступа к игре " + gameId);
        }

        spaceBattleService.runCommand(commandDTO);

        return spaceBattleService.getGameObject(commandDTO.getGameObjectId());
    }


    @GetMapping("/createGame")
    public String createGame(@RequestParam List<Integer> users, Authentication authentication) {
        if (!authentication.getAuthorities().contains(Role.ADMIN)) {
            throw new AuthError("только админ может создавать игру");
        }

        String gameId = UUID.randomUUID().toString();
        spaceBattleService.createGame(users, gameId);

        usersOfGame.put(gameId, users);

        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8095)
                .path("/users/registerInGame")
                .queryParam("gamezId", gameId)
                .queryParam("users", users)
                .toUriString();

        try {
            return new RestTemplate().getForObject(url, String.class);
        } catch (Exception e) {
            // Логика обработки ошибок или возвращение сообщения
            return "ошибка создания игры (этап регистрации игроков в auth): " + e.getMessage();
        }
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class AuthError extends RuntimeException {

        public AuthError(String message) {
            super(message);
        }

    }

}
