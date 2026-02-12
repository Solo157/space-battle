package com.space;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.space.api.dto.CommandDTO;
import com.space.service.CommandType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommandApiControllerTest {

    @Autowired
    private MockMvc mvc;

    // Пример теста
    @Test
    public void testRunCommand() throws Exception {
        // Создаем объект CommandDTO
        CommandDTO requestDto = new CommandDTO();
        requestDto.setCommandId(CommandType.MOVE_COMMAND.name());
        requestDto.setGameObjectId("objectId1");
        requestDto.setGameId("gameId1");
        requestDto.setArgs("{\"SetVelocity\": 3}");
        // добавьте остальные поля по необходимости

        // Конвертируем объект в JSON
        ObjectMapper mapper = new ObjectMapper();
        String jsonContent = mapper.writeValueAsString(requestDto);

        // Токен авторизации (например, тестовый JWT)
        var token = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzdGVwYSIsImdhbWVJc0FjY2Vzc2libGUiOnRydWUsInByaXZpbGVnZWQiOmZhbHNlLCJhZG1pbiI6ZmFsc2V9.GfOHLcg6nqv3Z9y32LLL6eIWm5hKXDALUnRRjmIbMbDww-x7gpCjN4nJB8P6VfGpeamUcNVQjg_k84qC394HyNBMDKI59ferlgyGJmcrnl9Erh_qQKB-mHuszMbZ4Xt8HS7l5yHmpIVKQEVbfa5MjnpynxwU9Qzu4qBWC18fv8ru4SNSR3VvDsuKeCFbPH3H0eRQItojIkibPMNIoZw09B69nNIBlzagfNweA7Tky5Iu0dsaK2qFYYRBqAj4M5Yvj1_KAk7lVSmxbP9C1P2IpEoPJwm5YYNEtU6uxh1dQ0uNpb237yhSFNFdoGG7RQWNnjjO5RQYAiPYAroXP_6XIA";

        // Выполняем POST-запрос
        mvc.perform(get("/runCommand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(jsonContent)
                )
                .andExpect(status().isOk());
    }
}
