package com.space;

import com.space.adapter.IMovingObject;
import com.space.command.ICommand;
import com.space.entity.Point;
import com.space.entity.Vector;
import com.space.ioc.IoC;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Класс по генерации адаптера для интерфеса IMovingObject.
 */
public class IMovingObjectGenerateAdapterService {

    private static Object adapter = null;

    public Object getAdapter(Object obj) {
        synchronized (this) {

            if (adapter != null) {
                return adapter;
            }

            InvocationHandler handler = new InvocationHandler() {

                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    switch (method.getName()) {
                        case "getLocation" -> {
                            return IoC.<Point>resolve("IMovingObject.Location", obj);
                        }
                        case "getVelocity" -> {
                            return IoC.<Vector>resolve("IMovingObject.Velocity", obj);
                        }
                        case "setPosition" ->
                                IoC.<ICommand>resolve("IMovingObject.Position.set", obj, args[0]).execute();
                        case "isStatic" -> {
                            return IoC.<Boolean>resolve("IMovingObject.Static", obj);
                        }
                    }

                    return null;
                }
            };

            adapter = Proxy.newProxyInstance(
                    IMovingObject.class.getClassLoader(),
                    new Class[]{IMovingObject.class},
                    handler
            );

            return adapter;
        }
    }

}
