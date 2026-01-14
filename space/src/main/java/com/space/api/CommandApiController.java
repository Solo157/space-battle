package com.space.api;

import com.space.api.dto.CommandDTO;
import com.space.service.SpaceBattleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/")
@RequiredArgsConstructor
public class CommandApiController {

    private final SpaceBattleService spaceBattleService;

    /**
     * Выполнить команду.
     */
    @PostMapping("runCommand")
    void runCommand(@RequestBody CommandDTO commandDTO) {
        spaceBattleService.runCommand(commandDTO);
    }

}
