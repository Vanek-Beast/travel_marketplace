package com.example.marketplace.service;

import com.example.marketplace.model.Route;
import com.example.marketplace.repository.RouteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    // Получение всех маршрутов
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    // Получение маршрута по id
    public Route getRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route not found by id=%s"
                        .formatted(id)));
    }

    // Создание маршрута
    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }

    // Обновление маршрута
    @Transactional
    public Route updateRoute(Long id, Route updatedRoute) {
         var route = routeRepository.findById(id)
                 .orElseThrow(() -> new EntityNotFoundException("Route not found by id=%s"
                         .formatted(id)));
         route.setOrigin(updatedRoute.getOrigin());
         route.setDestination(updatedRoute.getDestination());
         return route;
    }

    // Удаление маршрута
    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }
}
