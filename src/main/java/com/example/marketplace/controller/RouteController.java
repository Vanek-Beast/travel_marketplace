package com.example.marketplace.controller;

import com.example.marketplace.model.Route;
import com.example.marketplace.service.RouteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    // Получение всех маршрутов
    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    // Получение маршрута по id
    @GetMapping("/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    // Создание маршрута
    @PostMapping
    public ResponseEntity<Route> createRoute(@Valid @RequestBody Route route) {
        return ResponseEntity.status(HttpStatus.CREATED).body(routeService.createRoute(route));
    }

    // Обновление маршрута
    @PutMapping("/{id}")
    public ResponseEntity<Route> updateRoute(@PathVariable @Min(1) Long id, @Valid @RequestBody Route route) {
        return ResponseEntity.ok(routeService.updateRoute(id, route));
    }

    // Удаление маршрута
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable @Min(1) Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
}
