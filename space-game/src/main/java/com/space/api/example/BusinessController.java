package com.space.api.example;

import com.space.auth.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * КЛАСС СОЗДАН ИСКЛЮЧИТЕЛЬНО В ТЕСТОВЫХ ЦЕЛЯХ. К ЗАДАЧЕ КОСМИЧСКОГО БОЯ ОТНОШЕНИЯ НЕ ИМЕЕТ.
 */
@RestController
public class BusinessController {

    @GetMapping("/calculate")
    public Answer calculate(@RequestParam Integer a, @RequestParam Integer b, Authentication authentication) {
        SecurityContext context = SecurityContextHolder.getContext();
        if (authentication.getAuthorities().contains(Role.ADMIN)) {

        } else if (authentication.getAuthorities().contains(Role.PRIVILEGED)) {
            if (a < 0 || a > 100 || b < 0 || b > 100)
                throw new AuthError("Недостаточно прав - привилегированный пользователь");
        } else {
            if (a < 0 || a > 10 || b < 0 || b > 10)
                throw new AuthError("Недостаточно прав - пользователь");
        }

        return new Answer(authentication.getName(), (a + b));
    }

    @Data
    @AllArgsConstructor
    public static class Answer {
        private String user;
        private Integer answer;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class AuthError extends RuntimeException {

        public AuthError(String message) {
            super(message);
        }

    }

}
