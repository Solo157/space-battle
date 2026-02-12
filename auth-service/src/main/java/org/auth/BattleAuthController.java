package org.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class BattleAuthController {

    private static final List<User> users = List.of(
            new User(1, "vasya", true, "123", true), // только он может создавать игры
            new User(2, "petya", false, "123", true),
            new User(3, "stepa", false, "123", false)
    );

    // key - id игры, value - список участников игры
    private static final Map<String, List<Integer>> usersOfGame = new HashMap<>();

    private final JwtCreater jwtCreater;

    @GetMapping("/authenticate")
    public String authenticate(String user, String password) {
        return users.stream()
                .filter(dbUser -> dbUser.getUsername().equals(user) && dbUser.getPassword().equals(password))
                .findFirst()
                .map(jwtCreater::createJwt)
                .orElseThrow(AuthError::new);
    }

    @GetMapping("/authenticateForGame")
    public String authenticateForGame(String user, String password, String gameId) {
        Optional<User> userDbOpt = users.stream()
                .filter(dbUser -> dbUser.getUsername().equals(user) && dbUser.getPassword().equals(password))
                .findFirst();
        if (userDbOpt.isEmpty()) {
            throw new AuthError("вас нет в бд");
        }

        if (!usersOfGame.containsKey(gameId)) {
            throw new AuthError("такой игры нет");
        }

        User userDb = userDbOpt.get();
        if (!usersOfGame.get(gameId).contains(userDb.getId())) {
            throw new AuthError("вас нет в списке для игры " + gameId);
        }

        return jwtCreater.createJwtForSpaceBattle(userDb);
    }

    @GetMapping("/users/registerInGame")
    public String registerInGame(@RequestParam String gamezId, @RequestParam List<Integer> users) {
        usersOfGame.put(gamezId, users);
        return "registered in " + gamezId;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class AuthError extends RuntimeException {

        public AuthError(String message) {
            super(message);
        }

        public AuthError() {
        }

    }

}
