import com.space.adapter.MovingObjectAdapter;
import com.space.command.*;
import com.space.exception.exception.StaticObjectException;
import com.space.exception.hendler.ExceptionHandler;
import com.space.exception.hendler.MoveNullPointerExceptionHandler;
import com.space.exception.hendler.MoveStaticObjectExceptionHandler;
import com.space.entity.*;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SpaceShipTest {

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
        MoveCommand moveCommand = new MoveCommand(movingObjectAdapter);
        moveCommand.execute();

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
        MoveCommand moveCommand = new MoveCommand(movingObjectAdapter);
        assertThrows(RuntimeException.class, moveCommand::execute);
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
        MoveCommand moveCommand = new MoveCommand(movingObjectAdapter);
        assertThrows(RuntimeException.class, moveCommand::execute);
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
        MoveCommand moveCommand = new MoveCommand(movingObjectAdapter);
        assertThrows(RuntimeException.class, moveCommand::execute);
    }

    /**
     * Тест, проверяющий, что при исключении NPE происходит повторная отправка команды и при неуспешной - запись в лог.
     */
    @Test
    public void executeMove_repeatCommandOneTime_successfully() {
        // when
        LogExceptionCommand mockLogExceptionCommand = Mockito.spy(LogExceptionCommand.class);
        RetryCommandOneTime retryCommandOneTime = new RetryCommandOneTime(mockLogExceptionCommand);
        MoveNullPointerExceptionHandler moveNullPointerExceptionHandler = new MoveNullPointerExceptionHandler(retryCommandOneTime);

        ExceptionHandler.register(
                MoveCommand.class.toString(),
                NullPointerException.class.toString(),
                moveNullPointerExceptionHandler);

        MovingObjectAdapter movingObjectAdapter = new MovingObjectAdapter(null);

        // do
        ICommand move = Mockito.spy(new MoveCommand(movingObjectAdapter));
        try {
            move.execute();
        } catch (Exception e) {
            ExceptionHandler.handle(move, e).execute();
        }

        // assert
        verify(mockLogExceptionCommand).execute();
        verify(move, times(2)).execute();
    }

    /**
     * Тест, проверяющий, что при исключении StaticObjectException происходит две попытки выполнить команду
     * и после двух неуспешных - запись в лог.
     */
    @Test
    public void executeMove_repeatCommandTwoTimes_successfully() {
        // when
        LogExceptionCommand mockLogExceptionCommand = Mockito.spy(LogExceptionCommand.class);
        RetryCommandTwoTime retryCommandTwoTime = new RetryCommandTwoTime(mockLogExceptionCommand);
        MoveStaticObjectExceptionHandler moveStaticObjectExceptionHandler = new MoveStaticObjectExceptionHandler(retryCommandTwoTime);

        ExceptionHandler.register(
                MoveCommand.class.toString(),
                StaticObjectException.class.toString(),
                moveStaticObjectExceptionHandler);

        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setProperty("static", Boolean.TRUE);
        MovingObjectAdapter movingObjectAdapter = new MovingObjectAdapter(spaceShip);

        // do
        ICommand move = Mockito.spy(new MoveCommand(movingObjectAdapter));
        try {
            move.execute();
        } catch (Exception e) {
            ExceptionHandler.handle(move, e).execute();
        }

        // assert
        verify(mockLogExceptionCommand).execute();
        verify(move, times(3)).execute();
    }

}
