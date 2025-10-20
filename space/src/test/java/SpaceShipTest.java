import com.space.module.MovingObjectAdapter;
import com.space.module.SpaceShip;
import com.space.module.Move;
import com.space.module.Point;
import com.space.module.Vector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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

}
