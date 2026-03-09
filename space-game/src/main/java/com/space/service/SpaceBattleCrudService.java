package com.space.service;

import com.space.command.CollisionMacroCommand;
import com.space.entity.*;
import com.space.entity.Vector;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Класс имитирует работу с БД. Да, это statefull приложение, но работа с БД пока что не требуется.
 */
@Service
@Data
public class SpaceBattleCrudService {

    private static SpaceShip spaceShip1;
    private static SpaceShip spaceShip2;
    private static SpaceShip spaceShip3;
    private static SpaceShip spaceShip4;
    private static SpaceShip spaceShip5;
    private static SpaceShip spaceShip6;
    private static SpaceShip spaceShip7;
    private static SpaceShip spaceShip8;

    /**
     * Найти все идентификаторы объектов которыми игрок может управлять. Т.к. полноценного CRUD нет, то он возвращает
     * замоканный список.
     */
    public List<String> findAllObjectIds(String userId) {
        return List.of("objectId8");
    }

    public UObject findSpaceBattleObject(String objectId) {
        if (objectId.equals("objectId1")) {
            if (spaceShip1 != null) {
                return spaceShip1;
            }

            SpaceShip spaceShip = new SpaceShip();
            spaceShip.setProperty("id", "objectId1");
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
            spaceShip.setProperty("id", "objectId2");
            spaceShip.setProperty("velocity", new Vector(-5, 5));
            spaceShip.setProperty("location", new Point(14, 7));
            spaceShip.setProperty("position", new Point(7, 8));
            spaceShip.setProperty("static", Boolean.FALSE);

            this.spaceShip2 = spaceShip;

            return spaceShip;
        }

        if (objectId.equals("objectId3")) {
            if (spaceShip3 != null) {
                return spaceShip3;
            }

            SpaceShip spaceShip = new SpaceShip();
            spaceShip.setProperty("id", "objectId3");
            spaceShip.setProperty("velocity", new Vector(10, 0));
            spaceShip.setProperty("location", new Point(2, -5));
            spaceShip.setProperty("static", Boolean.FALSE);
            spaceShip.setProperty("macroCommand", new CollisionMacroCommand(new ArrayList<>()));

            this.spaceShip3 = spaceShip;

            return spaceShip;
        }

        if (objectId.equals("objectId4")) {
            if (spaceShip4 != null) {
                return spaceShip4;
            }

            SpaceShip spaceShip = new SpaceShip();
            spaceShip.setProperty("id", "objectId4");
            spaceShip.setProperty("velocity", new Vector(1, 1));
            spaceShip.setProperty("location", new Point(18, -5));
            spaceShip.setProperty("static", Boolean.FALSE);
            spaceShip.setProperty("macroCommand", new CollisionMacroCommand(new ArrayList<>()));

            this.spaceShip4 = spaceShip;

            return spaceShip;
        }

        if (objectId.equals("objectId5")) {
            if (spaceShip5 != null) {
                return spaceShip5;
            }

            SpaceShip spaceShip = new SpaceShip();
            spaceShip.setProperty("id", "objectId5");
            spaceShip.setProperty("velocity", new Vector(5, 0));
            spaceShip.setProperty("location", new Point(2, -5));
            spaceShip.setProperty("static", Boolean.FALSE);
            spaceShip.setProperty("macroCommand", new CollisionMacroCommand(new ArrayList<>()));

            this.spaceShip5 = spaceShip;

            return spaceShip;
        }

        if (objectId.equals("objectId6")) {
            if (spaceShip6 != null) {
                return spaceShip6;
            }

            SpaceShip spaceShip = new SpaceShip();
            spaceShip.setProperty("id", "objectId6");
            spaceShip.setProperty("velocity", new Vector(1, 1));
            spaceShip.setProperty("location", new Point(7, 0));
            spaceShip.setProperty("static", Boolean.FALSE);
            spaceShip.setProperty("macroCommand", new CollisionMacroCommand(new ArrayList<>()));

            this.spaceShip6 = spaceShip;

            return spaceShip;
        }

        if (objectId.equals("objectId7")) {
            if (spaceShip7 != null) {
                return spaceShip7;
            }

            SpaceShip spaceShip = new SpaceShip();
            spaceShip.setProperty("id", "objectId7");
            spaceShip.setProperty("velocity", new Vector(1, 1));
            spaceShip.setProperty("location", new Point(12, -5));
            spaceShip.setProperty("static", Boolean.FALSE);
            spaceShip.setProperty("macroCommand", new CollisionMacroCommand(new ArrayList<>()));

            this.spaceShip7 = spaceShip;

            return spaceShip;
        }

        if (objectId.equals("objectId8")) {
            if (spaceShip8 != null) {
                return spaceShip8;
            }

            SpaceShip spaceShip = new SpaceShip();
            spaceShip.setProperty("id", "objectId8");
            spaceShip.setProperty("owner", "545");
            spaceShip.setProperty("fuel", 2);
            spaceShip.setProperty("velocity", new Vector(-7, 3));
            spaceShip.setProperty("location", new Point(12, 5));
            spaceShip.setProperty("fuelFlowRate", 2);
            spaceShip.setProperty("static", Boolean.FALSE);
            spaceShip.setProperty("macroCommand", new CollisionMacroCommand(new ArrayList<>()));

            this.spaceShip8 = spaceShip;

            return spaceShip;
        }

        return null;
    }

    /**
     * Сервисный класс для сброса объектов в изначальное состояние. ИСПОЛЬЗУЕТСЯ ТОЛЬКО ДЛЯ ТЕСТОВ.
     * При нормальной реализации CRUD сервиса его бы не было.
     */
    public void resetDefaultCrudService() {
        spaceShip1 = null;
        spaceShip2 = null;
        spaceShip3 = null;
        spaceShip4 = null;
        spaceShip5 = null;
        spaceShip6 = null;
        spaceShip7 = null;
        spaceShip8 = null;
    }

}
