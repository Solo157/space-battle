import com.space.command.*;
import com.space.exception.exception.StaticObjectException;
import com.space.exception.hendler.ExceptionHandler;
import com.space.exception.hendler.MoveNullPointerExceptionHandler;
import com.space.exception.hendler.MoveStaticObjectExceptionHandler;
import com.space.module.*;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SpaceShipTest {

    void setUp() {
//        ExceptionHandler.register(Move.class.toString(), NullPointerException.class.toString(), new NullPointerExceptionHandler());

//        BiFunction<ICommand, Exception, ICommand> biFuncForIncorrectArgException = (cmd, e) -> {
//            cmd.getObject().setPosition(new Point(12, 5));
//            return cmd;
//        };
//        ExceptionHandler.register(Move.class.toString(), IncorrectArgumentException.class.toString(), biFuncForIncorrectArgException);
    }

    /**
     * Для объекта, находящегося в точке (12, 5) и движущегося со скоростью (-7, 3)
     * движение меняет положение объекта на (5, 8).
     */
    @Test
    public void executeMove_successfully() {
        // when
        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setProperty("velocity", new Vector(-7, 3));
        spaceShip.setProperty("location", new Point(12, 5));

        MovingObjectAdapter movingObjectAdapter = new MovingObjectAdapter(spaceShip);

        // do
        Move move = new Move(movingObjectAdapter);
        move.execute();

        // assert
        Point location = (Point) spaceShip.getProperty("location");
        assertEquals(5, location.getX());
        assertEquals(8, location.getY());
    }

    /**
     * Попытка сдвинуть объект, у которого невозможно прочитать положение в пространстве, приводит к ошибке.
     */
    @Test
    public void executeMove_unknownPosition_successfully() {
        // when
        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setProperty("velocity", new Vector(-7, 3));

        MovingObjectAdapter movingObjectAdapter = new MovingObjectAdapter(spaceShip);

        // do
        Move move = new Move(movingObjectAdapter);
        assertThrows(RuntimeException.class, move::execute);
    }

    /**
     * Попытка сдвинуть объект, у которого невозможно прочитать значение мгновенной скорости, приводит к ошибке.
     */
    @Test
    public void executeMove_unknownVelocity_successfully() {
        // when
        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setProperty("location", new Point(12, 5));

        MovingObjectAdapter movingObjectAdapter = new MovingObjectAdapter(spaceShip);

        // do
        Move move = new Move(movingObjectAdapter);
        assertThrows(RuntimeException.class, move::execute);
    }

    /**
     * Попытка сдвинуть объект, у которого невозможно изменить положение в пространстве, приводит к ошибке.
     */
    @Test
    public void executeMove_objectIsStatic_successfully() {
        // when
        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setProperty("velocity", new Vector(-7, 3));
        spaceShip.setProperty("location", new Point(12, 5));
        spaceShip.setProperty("static", Boolean.TRUE);

        MovingObjectAdapter movingObjectAdapter = new MovingObjectAdapter(spaceShip);

        // do
        Move move = new Move(movingObjectAdapter);
        assertThrows(RuntimeException.class, move::execute);
    }


//    /**
//     * Попытка сдвинуть объект, у которого невозможно изменить положение в пространстве, приводит к ошибке.
//     */
//    @Test
//    public void executeMove_objectIsStatic_successfully2() {
//        setUp();
//
//        // when
//        SpaceShip spaceShip = new SpaceShip();
//        spaceShip.setProperty("velocity", new Vector(-7, 3));
//        spaceShip.setProperty("location", new Point(12, 5));
//        spaceShip.setProperty("static", Boolean.TRUE);
//
//        MovingObjectAdapter movingObjectAdapter = new MovingObjectAdapter(spaceShip);
//
//        // do
//        ICommand move = new Move(movingObjectAdapter);
//
//        try {
//            move.execute();
//        } catch (Exception e) {
//            ExceptionHandler.handle(move, e).execute();
//        }
//
//        assertFalse((Boolean) spaceShip.getProperty("static"));
//    }
//
//    /**
//     * Попытка сдвинуть объект, у которого невозможно изменить положение в пространстве, приводит к ошибке.
//     */
//    @Test
//    public void executeMove_objectIsStatic_successfully3() {
//        setUp();
//
//        // when
//        SpaceShip spaceShip = new SpaceShip();
//        spaceShip.setProperty("velocity", new Vector(-7, 3));
//        spaceShip.setProperty("static", Boolean.FALSE);
//
//        MovingObjectAdapter movingObjectAdapter = new MovingObjectAdapter(spaceShip);
//
//        // do
//        ICommand move = new Move(movingObjectAdapter);
//
//        try {
//            move.execute();
//        } catch (Exception e) {
//            ExceptionHandler.handle(move, e).execute();
//        }
//
//        assertEquals(5, ((Point)spaceShip.getProperty("location")).getX());
//        assertEquals(8, ((Point)spaceShip.getProperty("location")).getY());
//    }

    /**
     * Попытка сдвинуть объект, у которого невозможно изменить положение в пространстве, приводит к ошибке.
     */
    @Test
    public void executeMove_objectIsStatic_successfully3() {
        // when
        LogExceptionCommand mockLogExceptionCommand = Mockito.spy(LogExceptionCommand.class);
        RetryCommandOneTime retryCommandOneTime = new RetryCommandOneTime();
        MoveNullPointerExceptionHandler moveNullPointerExceptionHandler = new MoveNullPointerExceptionHandler(mockLogExceptionCommand, retryCommandOneTime);

        ExceptionHandler.register(
                Move.class.toString(),
                NullPointerException.class.toString(),
                moveNullPointerExceptionHandler);

        MovingObjectAdapter movingObjectAdapter = new MovingObjectAdapter(null);

        // do
        ICommand move = Mockito.spy(new Move(movingObjectAdapter));
        try {
            move.execute();
        } catch (Exception e) {
            ExceptionHandler.handle(move, e);
        }

        // assert
        verify(mockLogExceptionCommand).execute();
        verify(move, times(2)).execute();
    }

    /**
     * Попытка сдвинуть объект, у которого невозможно изменить положение в пространстве, приводит к ошибке.
     */
    @Test
    public void executeMove_objectIsStatic_successfully4() {
        // when
        LogExceptionCommand mockLogExceptionCommand = Mockito.spy(LogExceptionCommand.class);
        RetryCommandTwoTime retryCommandTwoTime = new RetryCommandTwoTime();
        MoveStaticObjectExceptionHandler moveStaticObjectExceptionHandler = new MoveStaticObjectExceptionHandler(mockLogExceptionCommand, retryCommandTwoTime);

        ExceptionHandler.register(
                Move.class.toString(),
                StaticObjectException.class.toString(),
                moveStaticObjectExceptionHandler);

        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setProperty("static", Boolean.TRUE);
        MovingObjectAdapter movingObjectAdapter = new MovingObjectAdapter(spaceShip);

        // do
        ICommand move = Mockito.spy(new Move(movingObjectAdapter));
        try {
            move.execute();
        } catch (Exception e) {
            ExceptionHandler.handle(move, e);
        }

        // assert
        verify(mockLogExceptionCommand).execute();
        verify(move, times(3)).execute();
    }

}
