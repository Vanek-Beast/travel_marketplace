package com.example.marketplace.controller;

import com.example.marketplace.dto.PaymentDTO;
import com.example.marketplace.model.Payment;
import com.example.marketplace.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Получение платежа по id
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    // Получение платежа по bookingId
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<Payment> getPaymentByBookingId(@PathVariable @Min(1) Long bookingId) {
        return ResponseEntity.ok(paymentService.getByBookingId(bookingId));
    }

    // Обработка платежа
    @PatchMapping("/process/{id}")
    public ResponseEntity<Payment> processPayment(@PathVariable @Min(1) Long id, @Valid @RequestBody PaymentDTO paymentDTO) {
        return ResponseEntity.ok(paymentService.processPayment(id, paymentDTO));
    }
}