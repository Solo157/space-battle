package com.space.serverthread;

import com.space.IMovingObjectGenerateAdapterService;
import com.space.IMovingObjectMethodsRegister;
import com.space.adapter.IMovingObject;
import com.space.command.*;
import com.space.entity.UObject;
import com.space.entity.Vector;
import com.space.ioc.IoC;
import com.space.service.CommandType;
import com.space.service.SpaceBattleCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.function.Function;

@Component
public class ServerThreadIoCDependencyRegistrator {

    private static SpaceBattleCrudService crudService;

    @Autowired
    public ServerThreadIoCDependencyRegistrator(SpaceBattleCrudService spaceBattleCrudService) {
        crudService = spaceBattleCrudService;
    }

    /**
     * Регистрация зависимостей в IoC-контейнере для каждой игры.
     */
    public static void registerDependency(BlockingQueue<ICommand> queue) {
        initIoCScope();

        registerExceptionHandler();
        registerIMovingObjectAdapter();
        registerGameObjects();
        registerSetVelocityMethod();
        registerMoveCommand();
        registerPrintCommand();
        registerCommandQueue(queue);
    }

    private static void registerCommandQueue(BlockingQueue<ICommand> queue) {
        IoC.<ICommand>resolve("IoC.Register", "CommandQueue", (Function<Object[], Object>) (args) -> {
            ICommand iCommand = (ICommand) args[0];
            queue.add(iCommand);
            return new EmptyCommand();
        }).execute();
    }

    private static void registerPrintCommand() {
        IoC.<ICommand>resolve("IoC.Register", CommandType.PRINT_COMMAND.name(), (Function<Object[], Object>) (args) -> {
            return new PrintCommand();
        }).execute();
    }

    private static void registerMoveCommand() {
        IoC.<ICommand>resolve("IoC.Register", CommandType.MOVE_COMMAND.name(), (Function<Object[], Object>) (args) -> {
            UObject object = (UObject) args[0];
            var adapter = IoC.<IMovingObject>resolve("IMovingObjectAdapter", IMovingObject.class, object);
            return new MoveCommand(adapter);
        }).execute();
    }

    private static void registerSetVelocityMethod() {
        IoC.<ICommand>resolve("IoC.Register", "SetVelocity", (Function<Object[], Object>) (args) -> {
            UObject object = (UObject) args[0];
            Integer velocityDelta = Integer.parseInt((String) args[1]);

            var adapter = IoC.<IMovingObject>resolve("IMovingObjectAdapter", IMovingObject.class, object);
            Vector velocity = adapter.getVelocity();
            int nextVelocityX = velocity.getX() + velocityDelta;
            int nextVelocityY = velocity.getY() + velocityDelta;

            adapter.setVelocity(new Vector(nextVelocityX, nextVelocityY));

            return (Object) adapter;
        }).execute();
    }

    private static void registerGameObjects() {
        IoC.<ICommand>resolve("IoC.Register", "GameObjects", (Function<Object[], Object>) (args) -> {
            String objectId = args[0].toString();
            UObject spaceBattleObject = crudService.findSpaceBattleObject(objectId);
            return (Object) spaceBattleObject;

        }).execute();
    }

    private static void registerIMovingObjectAdapter() {
        IMovingObjectGenerateAdapterService adapterService = new IMovingObjectGenerateAdapterService();
        IoC.<ICommand>resolve("IoC.Register", "IMovingObjectAdapter",
                        (Function<Object[], Object>) (args) -> {
                            return (IMovingObject) adapterService.getAdapter(args[1]);
                        })
                .execute();
        IMovingObjectMethodsRegister.registerIMovingObjectMethods();
    }

    private static void registerExceptionHandler() {
        IoC.<ICommand>resolve("IoC.Register", "ExceptionHandler", (Function<Object[], Object>) (args) -> {
            return (Object) args[0];
        }).execute();
    }

    private static void initIoCScope() {
        new InitCommand().execute();
        var iocScope = IoC.<Object>resolve("IoC.Scope.Create");
        IoC.<ICommand>resolve("IoC.Scope.Current.Set", iocScope).execute();
    }

}
