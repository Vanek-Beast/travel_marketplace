package com.example.marketplace.controller;

import com.example.marketplace.dto.TripDTO;
import com.example.marketplace.dto.TripSearchDTO;
import com.example.marketplace.model.Trip;
import com.example.marketplace.service.TripService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Validated
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
    public ResponseEntity<Trip> getTripById(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(tripService.getTripById(id));
    }

    // Получение поездок по маршруту
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<Trip>> getTripsByRouteId(@PathVariable @Min(1) Long routeId) {
        return ResponseEntity.ok(tripService.getTripsByRouteId(routeId));
    }

    // Получение поездок по типу транспорта
    @GetMapping("/transport-type/{transportTypeId}")
    public ResponseEntity<List<Trip>> getTripsByTransportTypeId(@PathVariable @Min(1) Long transportTypeId) {
        return ResponseEntity.ok(tripService.getTripsByTransportTypeId(transportTypeId));
    }

    @GetMapping("/nearest-trips")
    public ResponseEntity<List<Trip>> getNearestTrips(@RequestBody TripSearchDTO tripSearchDTO) {
        return ResponseEntity.ok(tripService.getNearestTrips(tripSearchDTO));
    }

    @GetMapping("/grouped-trips")
    public ResponseEntity<Map<LocalDate, List<Trip>>> getGroupedTrips(@RequestBody TripSearchDTO tripSearchDTO) {
        return ResponseEntity.ok(tripService.getTripsGroupedByDate(tripSearchDTO));
    }

    // Создание новой поездки
    @PostMapping
    public ResponseEntity<Trip> createTrip(@Valid @RequestBody TripDTO tripDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.createTrip(tripDTO));
    }

    // Обновление поездки
    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable @Min(1) Long id, @Valid @RequestBody TripDTO tripDTO) {
        return ResponseEntity.ok(tripService.updateTrip(id, tripDTO));
    }

    // Удаление поездки
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable @Min(1) Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }
}