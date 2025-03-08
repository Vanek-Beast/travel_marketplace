package com.example.marketplace.service;

import com.example.marketplace.dto.PaymentDTO;
import com.example.marketplace.model.Booking;
import com.example.marketplace.model.Payment;
import com.example.marketplace.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Создание платежа
    public void createPayment(Booking booking, BigDecimal amount) {
        paymentRepository.save(Payment.builder()
                .booking(booking)
                .amount(amount)
                .build());

    }
    // Получение платежа по id
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id=%s".formatted(id)));
    }

    // Обработка платежа
    @Transactional
    public Payment processPayment(Long id, PaymentDTO paymentDTO) {
        Payment payment = getPaymentById(id);

        if (payment.getStatus() == Payment.PaymentStatus.PAID) {
            throw new IllegalStateException("Payment has already paid");
        }

        if (paymentDTO.getPaymentMethod() == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }

        // Устанавливаем метод оплаты
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());

        boolean isPaid = mockPaymentProcessing(); // Имитация платежа

        if (isPaid) {
            payment.setStatus(Payment.PaymentStatus.PAID);
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
        }

        return payment;
    }

    // Имитация платежа
    private boolean mockPaymentProcessing() {
        return Math.random() > 0.2; // 80% успеха, 20% неудачи
    }

    // Получение платежа по брони
    public Payment getByBookingId(Long id) {
        return paymentRepository.findByBookingId(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with booking_id=%s".formatted(id)));
    }

    // Возврат платежа
    @Transactional
    public void refundPayment(Long paymentId) {
        Payment payment = getPaymentById(paymentId);

        // Проверяем, можно ли выполнить возврат
        if (payment.getStatus() != Payment.PaymentStatus.PAID) {
            throw new IllegalStateException("Refunds are only possible for paid payments.");
        }

        if (Objects.equals(payment.getStatus(), Payment.PaymentStatus.REFUNDED)) {
            throw new IllegalStateException("Payment has already been refunded.");
        }

        payment.setStatus(Payment.PaymentStatus.REFUNDED);
    }
}
