package com.space.service;

import com.space.command.ICommand;
import com.space.command.InitCommand;
import com.space.ioc.IoC;
import com.space.serverthread.ServerThreadIoCDependencyRegistrator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

/**
 * Сервис для работы со скоупами пользователей/игроков. Управляет CRUD действиями по скопам.
 */
@Service
public class UserScopeService {

    private final ServerThreadIoCDependencyRegistrator registrator;
    private final SpaceBattleCrudService spaceBattleCrudService;
    public final Map<String, Object> userScopes = new HashMap<>();

    public UserScopeService(ServerThreadIoCDependencyRegistrator registrator,
                            SpaceBattleCrudService spaceBattleCrudService) {
        this.registrator = registrator;
        this.spaceBattleCrudService = spaceBattleCrudService;
    }

    /**
     * Создать скоп пользователя/игрока. (если игроков много и требуется для разных разный скоуп, то лучше делать данный
     * метод шаблонным методом и переопределять в наследниках, для создания скоупа для разных типов игроков).
     */
    public void createUserScope(String userId) {
        new InitCommand().execute();
        var parentScope = IoC.<Map<String, Function<Object[], Object>>>resolve("IoC.Scope.Create.Empty");
        var iocScope = IoC.<Object>resolve("IoC.Scope.Create", parentScope);
        IoC.<ICommand>resolve("IoC.Scope.Current.Set", iocScope).execute();

        ServerThreadIoCDependencyRegistrator.registerStartMoveCommand();
        registrator.registerGameObjectId(userId);
        userScopes.put(userId, iocScope);
    }

    /**
     * Получить скоуп для конкретного пользователя.
     */
    public Object getUserScope(String userId) {
        return userScopes.get(userId);
    }

}
