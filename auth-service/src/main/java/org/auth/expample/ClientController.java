package org.auth.expample;

import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * КЛАСС СОЗДАН ИСКЛЮЧИТЕЛЬНО В ТЕСТОВЫХ ЦЕЛЯХ. К ЗАДАЧЕ КОСМИЧСКОГО БОЯ ОТНОШЕНИЯ НЕ ИМЕЕТ.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private String token;

    private final RestTemplate restTemplate = new RestTemplate();

    {
        restTemplate.getInterceptors().add(new AuthenticationInterceptor());
    }

    @GetMapping("/queryToken")
    public String queryToken(String user, String password) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8095)
                .path("/authenticate")
                .queryParam("user", user)
                .queryParam("password", password)
                .toUriString();

        try {
            token = new RestTemplate().getForObject(url, String.class);
        } catch (Exception e) {
            return "Ошибка получения токена: " + e.getMessage();
        }

        return token;
    }

    private String currentToken() {
        if (token == null) {
            return "no token";
        }

        int lastPointPos = token.lastIndexOf('.');
        return Jwts.parserBuilder().build().parseClaimsJwt(token.substring(0, lastPointPos+1)).toString();
    }

    @GetMapping("/calculate")
    public Integer calculate(Integer a, Integer b) {
        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8075)
                .path("/calculate")
                .queryParam("a", a)
                .queryParam("b", b)
                .toUriString();

          Answer result = restTemplate.getForObject(url, Answer.class);
        return result.getAnswer();
    }

    @Data
    public static class Answer {
        private String user;
        private Integer answer;
    }

    private class AuthenticationInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            request.getHeaders().add("Authorization", "Bearer " + token);
            ClientHttpResponse response = execution.execute(request, body);
            return response;
        }

    }

}
