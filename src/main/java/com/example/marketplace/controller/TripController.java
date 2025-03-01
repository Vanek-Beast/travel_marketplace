package com.example.marketplace.controller;

import com.example.marketplace.dto.TripDTO;
import com.example.marketplace.model.Trip;
import com.example.marketplace.service.TripService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    // Получение всех поездок
    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }

    // Получение поездки по ID
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.getTripById(id));
    }

    // Получение поездок по маршруту
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<Trip>> getTripsByRouteId(@PathVariable Long routeId) {
        return ResponseEntity.ok(tripService.getTripsByRouteId(routeId));
    }

    // Получение поездок по типу транспорта
    @GetMapping("/transport-type/{transportTypeId}")
    public ResponseEntity<List<Trip>> getTripsByTransportTypeId(@PathVariable Long transportTypeId) {
        return ResponseEntity.ok(tripService.getTripsByTransportTypeId(transportTypeId));
    }

    // Проверка доступных мест для поездки
    @GetMapping("/{tripId}/available-seats")
    public ResponseEntity<Boolean> checkAvailableSeats(@PathVariable Long tripId) {
        return ResponseEntity.ok(tripService.hasAvailableSeats(tripId));
    }

    // Создание новой поездки
    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody TripDTO tripDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.createTrip(tripDTO));
    }

    // Обновление поездки
    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable Long id, @RequestBody TripDTO tripDTO) {
        return ResponseEntity.ok(tripService.updateTrip(id, tripDTO));
    }

    // Удаление поездки
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }
    // Глобальный обработчик ошибок
    @ExceptionHandler({EntityNotFoundException.class, IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}