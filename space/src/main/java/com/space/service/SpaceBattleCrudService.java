package com.space.service;

import com.space.entity.Point;
import com.space.entity.SpaceShip;
import com.space.entity.UObject;
import com.space.entity.Vector;
import org.springframework.stereotype.Service;

/**
 * Класс имитирует работу с БД. Да, это statefull приложение, но работа с БД пока что не требуется.
 */
@Service
public class SpaceBattleCrudService {

    private static SpaceShip spaceShip1;
    private static SpaceShip spaceShip2;

    public UObject findSpaceBattleObject(String objectId) {
        if (objectId.equals("objectId1")) {
            if (spaceShip1 != null) {
                return spaceShip1;
            }

            SpaceShip spaceShip = new SpaceShip();
            spaceShip.setProperty("velocity", new Vector(-7, 3));
            spaceShip.setProperty("location", new Point(12, 5));
            spaceShip.setProperty("position", new Point(5, 6));
            spaceShip.setProperty("static", Boolean.FALSE);

            this.spaceShip1 = spaceShip;

            return spaceShip;
        }

        if (objectId.equals("objectId2")) {
            if (spaceShip2 != null) {
                return spaceShip2;
            }

            SpaceShip spaceShip = new SpaceShip();
            spaceShip.setProperty("velocity", new Vector(-5, 5));
            spaceShip.setProperty("location", new Point(14, 7));
            spaceShip.setProperty("position", new Point(7, 8));
            spaceShip.setProperty("static", Boolean.FALSE);

            this.spaceShip2 = spaceShip;

            return spaceShip;
        }

        return null;
    }

}
