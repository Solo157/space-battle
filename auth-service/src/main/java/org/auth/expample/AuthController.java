package org.auth.expample;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * КЛАСС СОЗДАН ИСКЛЮЧИТЕЛЬНО В ТЕСТОВЫХ ЦЕЛЯХ. К ЗАДАЧЕ КОСМИЧСКОГО БОЯ ОТНОШЕНИЯ НЕ ИМЕЕТ.
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

//    private static final List<User> users = List.of(
//            new User(1,"vasya", true, "123", true),
//            new User(2, "petya", false, "123", true),
//            new User(3, "stepa", false, "123", false)
//    );
//
//    private final JwtCreater jwtCreater;
//
//    @GetMapping("/authenticate")
//    public String authenticate(String user, String password) {
//        return users.stream()
//                .filter(dbUser -> dbUser.getUsername().equals(user) && dbUser.getPassword().equals(password))
//                .findFirst()
//                .map(jwtCreater::createJwt)
//                .orElseThrow(AuthError::new);
//    }
//
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public static class AuthError extends RuntimeException {
//
//    }

}
