package com.space.handler;

import com.space.entity.NeighbourhoodSystem;
import com.space.entity.UObject;

import java.util.*;

/**
 * Обработчик для какой-то окрестности (neighborhood). Нужен, чтобы собрать всех соседей/объектов для объекта, который
 * в эту окрестность только пришел.
 */
public class NeighborhoodSystemHandler implements SystemHandlerHandler {

    /**
     * Окрестность в которой находятся соседи для объекта, который вошел в эту окрестность.
     */
    private NeighbourhoodSystem.Neighbourhood neighborhood;
    /**
     * Установить следующую окрестность для обработки. Если null, то заканчиваем перебор окрестностей.
     * По цепочки их обрабатываем. (цепочка обязанностей).
     */
    private SystemHandlerHandler next;

    public NeighborhoodSystemHandler() {
    }

    public NeighborhoodSystemHandler(NeighbourhoodSystem.Neighbourhood neighborhood) {
        this.neighborhood = neighborhood;
    }

    public NeighborhoodSystemHandler(NeighbourhoodSystem.Neighbourhood neighborhood, SystemHandlerHandler next) {
        this.neighborhood = neighborhood;
        this.next = next;
    }

    @Override
    public void handle(UObject object, List<UObject> neighbors) {
        var inside = this.neighborhood.isObjectInsideNeighbourhoodPart(object);

        // если внутри, то собираем соседей и кладем наш смещенный объект в данную окрестность, если не внутри, то
        // удаляем из окрестности
        if (inside) {
            fillNeighbors(neighbors);
            neighborhood.getUObjectMap().put(((String) object.getProperty("id")), object);
        } else {
            neighborhood.getUObjectMap().remove(((String) object.getProperty("id")));
        }

        if (next != null) next.handle(object, neighbors);
    }

    /**
     * Заполнить соседей этой окрестности.
     */
    private void fillNeighbors(List<UObject> neighbors) {
        List<String> neighborsIds = neighbors.stream().map(obj -> ((String) obj.getProperty("id"))).toList();

        for (Map.Entry<String, UObject> entry : this.neighborhood.getUObjectMap().entrySet()) {
            if (neighborsIds.contains(entry.getKey())) {
                continue;
            }

            neighbors.add(entry.getValue());
        }
    }

    @Override
    public void setNext(SystemHandlerHandler next) {
        this.next = next;
    }

}
