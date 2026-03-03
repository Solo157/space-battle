package com.space.command;

import com.space.entity.Point;
import com.space.entity.UObject;
import lombok.Data;

/**
 *  Команда определяет коллизию/столкновение двух объектов по их координатам.
 */
@Data
public class CollisionCheckCommand implements ICommand {

    private UObject obj1;
    private UObject obj2;
    private boolean isCollide = false;

    public CollisionCheckCommand(UObject obj1, UObject obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    @Override
    public void execute() {
        if(isCollide(obj1, obj2)) {
            System.out.println("Столкновение объектов!");
            this.isCollide = true;
        }
    }

    private boolean isCollide(UObject obj1, UObject obj2) {
        Point location = (Point) obj1.getProperty("location");
        if (location == null) {
            return false;
        }

        Point location1 = (Point) obj2.getProperty("location");
        if (location1 == null) {
            return false;
        }

        return location.getX() == location1.getX() && location.getY() == location1.getY();
    }

}
