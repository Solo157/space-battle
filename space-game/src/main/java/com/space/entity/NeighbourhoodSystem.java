package com.space.entity;

import lombok.Data;

import java.util.*;

/**
 * Система окрестностей. Поле игры можно разбить на множество окрестностей (например, квадраты), каждый такой квадрат -
 * neighbourhood(окрестность). Таких систем может быть несколько для одной игры, чтобы обрабатывать ситуации, когда
 * два объекта могут столкнуться на границах двух окрестностей одной системы окрестностей. Для этого создется второая
 * система и просто смотрится, если в первой системе каждый из них на границе, но они находятся в разных окрестностях, то
 * тогда это не считается за столкновение, но во второй системе окрестностей они будут находиться в одной окрестности
 * и поэтому их столкновение зафиксируется. Иными словами просто создается сначала сетка окрестностей, а под ней еще
 * одна сетка смещенная.
 */
@Data
public class NeighbourhoodSystem {

    /**
     * Список всех окрестностей для системы.
     */
    private final List<Neighbourhood> neighborhoods = new ArrayList<>();

    public NeighbourhoodSystem(List<Neighbourhood> neighbourhoods) {
        neighborhoods.addAll(neighbourhoods);
    }

    /**
     * Красс окрестности.
     */
    @Data
    public static class Neighbourhood {

        // хранит все объекты в данной окрестности
        // key - objectId, value - object
        private final Map<String, UObject> uObjectMap = new HashMap<>();

        // ниже координаты углов окрестности (в нашем случае квадрата).
        private final Point highLeft;
        private final Point highRight;
        private final Point lowLeft;
        private final Point lowRight;

        public Neighbourhood(Point highLeft, Point highRight, Point lowLeft, Point lowRight) {
            this.highLeft = highLeft;
            this.highRight = highRight;
            this.lowLeft = lowLeft;
            this.lowRight = lowRight;
        }

        /**
         * Проверка, входит ли объект внутрь этой окрестности.
         */
        public boolean isObjectInsideNeighbourhoodPart(UObject object) {
            Point location = (Point) object.getProperty("location");
            if (location == null) {
                return false;
            }

            return (location.getX() >= this.getHighLeft().getX() && location.getX() <= this.getHighRight().getX()
                    && location.getY() <= this.getHighLeft().getY() && location.getY() >= this.getLowLeft().getY());
        }

    }

}
