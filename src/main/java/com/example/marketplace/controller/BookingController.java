package com.example.marketplace.controller;

import com.example.marketplace.dto.BookingDTO;
import com.example.marketplace.model.Booking;
import com.example.marketplace.service.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Создание бронирования
    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.createBooking(bookingDTO));
    }

    // Отмена бронирования
    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<String> cancelBooking(@PathVariable @Min(1) Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok("Booking cancelled successfully");
    }

    // Получение всех броней пользователя
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable @Min(1) Long userId) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }

    // Получение информации о конкретной брони
    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBookingById(@PathVariable @Min(1) Long bookingId) {
        return ResponseEntity.ok(bookingService.getBookingById(bookingId));
    }
}