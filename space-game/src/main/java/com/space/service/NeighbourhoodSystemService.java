package com.space.service;

import com.space.entity.NeighbourhoodSystem;
import com.space.entity.Point;
import com.space.handler.NeighborhoodSystemHandler;
import com.space.handler.SystemHandlerHandler;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Сервис для работы с системами окрестностей.
 */
@Service
public class NeighbourhoodSystemService {

    // key - gameId, value - список систем окрестностей для игры
    private final Map<String, List<NeighbourhoodSystem>> systemMap = new HashMap<>();

    /**
     * Создание для игры систем окрестностей.
     */
    public void createSystem(String gameId) {
        var neighbourhood1ForSystem1 = new NeighbourhoodSystem.Neighbourhood(new Point(0, 0), new Point(10, 0), new Point(0, -10), new Point(10, -10));
        var neighbourhood2ForSystem1 = new NeighbourhoodSystem.Neighbourhood(new Point(10, 0), new Point(20, 0), new Point(10, -10), new Point(20, -10));
        NeighbourhoodSystem neighbourhoodSystem = new NeighbourhoodSystem(List.of(neighbourhood1ForSystem1, neighbourhood2ForSystem1));

        var neighbourhood3ForSystem2 = new NeighbourhoodSystem.Neighbourhood(new Point(5, 0), new Point(15, 0), new Point(5, -10), new Point(15, -10));
        NeighbourhoodSystem neighbourhoodSystem2 = new NeighbourhoodSystem(List.of(neighbourhood3ForSystem2));
        systemMap.put(gameId, List.of(neighbourhoodSystem, neighbourhoodSystem2));
    }

    /**
     * Полуить обработчики окрестностей для конкретной игры.
     */
    public SystemHandlerHandler getCollisionHandlers(String gameId) {
        List<SystemHandlerHandler> systemHandlerHandlers = systemMap.get(gameId).stream()
                .flatMap(system -> system.getNeighborhoods().stream())
                .map(neighbourhood -> (SystemHandlerHandler) new NeighborhoodSystemHandler(neighbourhood))
                .toList();

        // создаем цепочку из обработчиков окрестностей
        for (int i = 0; i < systemHandlerHandlers.size()-1; i++) {
            SystemHandlerHandler handler = systemHandlerHandlers.get(i);
            handler.setNext(systemHandlerHandlers.get(i+1));
        }

        return systemHandlerHandlers.get(0);
    }

    /**
     * Получить систему окрестностей для игры.
     */
    public List<NeighbourhoodSystem> getNeighbourhoodSystemByGameId(String gameId) {
        return systemMap.get(gameId);
    }

}
