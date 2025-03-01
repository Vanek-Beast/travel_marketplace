package com.example.marketplace.controller;

import com.example.marketplace.model.TransportType;
import com.example.marketplace.service.TransportTypeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transport-types")
public class TransportTypeController {

    private final TransportTypeService transportTypeService;

    @Autowired
    public TransportTypeController(TransportTypeService transportTypeService) {
        this.transportTypeService = transportTypeService;
    }

    // Получение всех видов транспорта
    @GetMapping
    public ResponseEntity<List<TransportType>> getAllTransportTypes() {
        return ResponseEntity.ok(transportTypeService.getAllTransportTypes());
    }

    // Получение вида транспорта по id
    @GetMapping("/{id}")
    public ResponseEntity<TransportType> getTransportTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(transportTypeService.getTransportTypeById(id));
    }

    // Создание вида транспорта
    @PostMapping
    public ResponseEntity<TransportType> createTransportType(@RequestBody TransportType transportType) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transportTypeService.createTransportType(transportType));
    }

    // Обновление вида транспорта
    @PutMapping("/{id}")
    public ResponseEntity<TransportType> updateTransportType(@PathVariable Long id, @RequestBody TransportType transportType) {
        return ResponseEntity.ok(transportTypeService.updateTransportType(id, transportType));
    }

    // Удаление вида транспорта
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransportType(@PathVariable Long id) {
        transportTypeService.deleteTransportType(id);
        return ResponseEntity.noContent().build();
    }
    // Глобальный обработчик ошибок
    @ExceptionHandler({EntityNotFoundException.class, IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
