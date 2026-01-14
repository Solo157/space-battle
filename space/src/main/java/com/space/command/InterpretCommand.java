package com.space.command;

import ch.qos.logback.core.util.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.space.api.dto.CommandDTO;
import com.space.entity.UObject;
import com.space.ioc.IoC;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Специальная команда - интерпретатор. В нее передаются данные по другой команде. Эти данные проверяются/валидируются
 * и затем находится команда и отдается на выполнение очереди.
 */
public class InterpretCommand implements ICommand {

    private static ObjectMapper mapper = new ObjectMapper();

    private String gameId;
    private String gameObjectId;
    private String commandId;
    private String args;

    public InterpretCommand(CommandDTO commandDTO) {
        this.gameId = commandDTO.getGameId();
        this.gameObjectId = commandDTO.getGameObjectId();
        this.commandId = commandDTO.getCommandId();
        this.args = commandDTO.getArgs();
    }

    @Override
    public void execute() {
        var uObject = IoC.<UObject>resolve("GameObjects", gameObjectId);

        validateArgsParameters(args);
        validateArgsParameters(args);
        applyArgsParametersToObject(uObject, args);
        validateCommandId(commandId);

        var cmd = IoC.<ICommand>resolve(commandId, uObject);
        IoC.<ICommand>resolve("CommandQueue", cmd).execute();
    }

    /**
     * Валидируем команду. Чтобы пользователь не смог сделать инъекцию.
     */
    public void validateCommandId(String operation) {
        return;
    }

    /**
     * Валидируем аргументы. Чтобы пользователь не смог сделать инъекцию.
     */
    public void validateArgsParameters(String args) {
        if (StringUtil.isNullOrEmpty(args)) {
            return;
        }

        // можно написать код, проверяющий аргументы.

        return;
    }

    public static void applyArgsParametersToObject(UObject uObject, String args) {
        if (StringUtil.isNullOrEmpty(args)) {
            return;
        }

        try {
            JsonNode rootNode = mapper.readTree(args);

            // Проверяем, что это объект
            if (rootNode.isObject()) {
                Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> field = fields.next();
                    String key = field.getKey();
                    JsonNode value = field.getValue();

                    // применяем параметры к объекту
                    IoC.resolve(key, uObject, value.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
