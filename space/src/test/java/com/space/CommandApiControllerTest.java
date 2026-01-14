package com.space;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.space.api.CommandApiController;
import com.space.api.dto.CommandDTO;
import com.space.serverthread.ServerThreadIoCDependencyRegistrator;
import com.space.service.CommandType;
import com.space.service.SpaceBattleCrudService;
import com.space.service.SpaceBattleService;
import com.space.service.games.SpaceBattleGame1;
import com.space.service.games.SpaceBattleGame2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommandApiController.class)
@Import({
        SpaceBattleService.class,
        SpaceBattleCrudService.class,
        ServerThreadIoCDependencyRegistrator.class,
        SpaceBattleGame1.class,
        SpaceBattleGame2.class
})
@AutoConfigureMockMvc
public class CommandApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SpaceBattleCrudService spaceBattleCrudService;

    @Test
    void asAnonymousForGetImpactReport_shouldReturn401() throws Throwable {
        // before
        CommandDTO requestDto = new CommandDTO();
        requestDto.setCommandId(CommandType.MOVE_COMMAND.name());
        requestDto.setGameObjectId("objectId1");
        requestDto.setGameId("gameId1");
        requestDto.setArgs("{\"SetVelocity\": 3}");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(requestDto);

        // do
        mvc.perform(post("/v1/runCommand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                )
                .andExpect(status().isOk());
    }

}
