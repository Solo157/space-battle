package com.space.exception.hendler;

import com.space.command.ICommand;

import java.util.*;
import java.util.function.BiFunction;

public class ExceptionHandler {

    private static Map<String, BiFunction<ICommand, Exception, ICommand>> store = new HashMap<>();

    public static ICommand handle(ICommand cmd, Exception e) {
        String strCmdClass = cmd.getClass().toString();
        String strExceptionClass = e.getClass().toString();

        String key = strCmdClass + strExceptionClass;
        return store.get(key).apply(cmd, e);
    }

    public static void register(String cmdClass,
                                String exceptionClass,
                                BiFunction<ICommand, Exception, ICommand> handler) {
        String key = cmdClass + exceptionClass;
        store.put(key, handler);
    }

}
