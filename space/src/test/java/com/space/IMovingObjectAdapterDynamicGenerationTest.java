package com.space;

import com.space.adapter.IMovingObject;
import com.space.command.ICommand;
import com.space.entity.Point;
import com.space.entity.SpaceShip;
import com.space.entity.Vector;
import com.space.ioc.IoC;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IMovingObjectAdapterDynamicGenerationTest {

    /**
     * Проверка работы создания адаптера "на лету".
     */
    @Test
    public void IMovingObjectAdapterCreation_successfully() {
//        setUp();

        IMovingObjectGenerateAdapterService adapterService = new IMovingObjectGenerateAdapterService();
        IoC.<ICommand>resolve("IoC.Register", "IMovingObjectAdapter",
                (Function<Object[], Object>) (args) -> (Object) adapterService.getAdapter(args[1])).execute();
        IMovingObjectMethodsRegister.registerIMovingObjectMethods();

        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setProperty("velocity", new Vector(-7, 3));
        spaceShip.setProperty("location", new Point(12, 5));
        spaceShip.setProperty("position", new Point(5, 6));
        spaceShip.setProperty("static", Boolean.TRUE);

        var adapter = IoC.<IMovingObject>resolve("IMovingObjectAdapter", IMovingObject.class, spaceShip);

        assertEquals(5, ((Point) spaceShip.getProperty("position")).getX());
        assertEquals(6, ((Point) spaceShip.getProperty("position")).getY());
        adapter.setPosition(new Point(7, 8));
        assertEquals(7, ((Point) spaceShip.getProperty("position")).getX());
        assertEquals(8, ((Point) spaceShip.getProperty("position")).getY());

        assertEquals(12, adapter.getLocation().getX());
        assertEquals(5, adapter.getLocation().getY());
        assertEquals(-7, adapter.getVelocity().getX());
        assertEquals(3, adapter.getVelocity().getY());
        assertEquals(Boolean.TRUE, adapter.isStatic());
    }

}

//    IMPORTANT_INFORMATION! DON'T REMOVE!
//        1. создаем адаптер.
//        2. прочитали методы интерфейса.
//3. Для каждого метода зарегали в IoC MovingObjectAdapter.Location чтобы получать значение из объекта или просто вызов метода
//        4. В каждом методе адаптера прописали resolve("MovingObjectAdapter.Location
//        string class = @"генерируемый класс"; obj кидаем в адаптер.
//            return IoC.<Point>resolve("MovingObjectAdapter.Location", obj);
//
//        // попутно генерации класса также генерируем класс фабрики, реализующий IFactory.
//        IFactory factory = Activator.<IFactory>createInstance("MovingObjectAdapterFactory");
//        // активатор используется один раз, чтобы создать фабрку. лямбда позволяет оптимизировать скорость создания  объекта
//        // не постоянно используем рефлексию, а один раз при создании фабрики.
//        IoC.<ICommand>resolve("IoC.Register", "IMovingObject", (object[] args) => factory.create(Map<string, object>) args[0]).execute();
//        var adapter = IoC.<IMovable>resolve("Adapter", typeof(IMovable), obj);
//        // будем генерировать по определению интерфейса.
//        T getProperty() -> return IoC.<T>resolve(<Interface name>.<Property>, obj);
//        // асинхронная реализация. создали команду, но выполнить можем хоть когда
//        void setProperty(T newValue) -> return IoC.<T>resolve(<Interface name>.<Property>.Set, obj, newValue).execute();