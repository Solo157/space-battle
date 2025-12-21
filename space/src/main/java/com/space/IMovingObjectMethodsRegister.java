package com.space;

import com.space.adapter.IMovingObject;
import com.space.command.EmptyCommand;
import com.space.command.ICommand;
import com.space.entity.UObject;
import com.space.ioc.IoC;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * Класс для регистрации методов интерфейса IMovingObject в IoC.
 */
public class IMovingObjectMethodsRegister {

    public static void registerIMovingObjectMethods() {
        for (Method method : IMovingObject.class.getDeclaredMethods()) {

            if (method.getName().equals("getLocation")) {
                IoC.<ICommand>resolve("IoC.Register", "IMovingObject.Location",
                        (Function<Object[], Object>) (args) -> (Object) ((UObject)args[0]).getProperty("location")).execute();
            }

            if (method.getName().equals("getVelocity")) {
                IoC.<ICommand>resolve("IoC.Register", "IMovingObject.Velocity",
                        (Function<Object[], Object>) (args) -> (Object) ((UObject)args[0]).getProperty("velocity")).execute();
            }

            if (method.getName().equals("setPosition")) {
                IoC.<ICommand>resolve("IoC.Register", "IMovingObject.Position.set",
                        (Function<Object[], Object>) (args) -> {
                            ((UObject)args[0]).setProperty("position", args[1]);
                            return new EmptyCommand();
                        }
                ).execute();
            }

            if (method.getName().equals("isStatic")) {
                IoC.<ICommand>resolve("IoC.Register", "IMovingObject.Static",
                        (Function<Object[], Object>) (args) -> (Object) ((UObject)args[0]).getProperty("static")).execute();
            }

        }

    }
}
