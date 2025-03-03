package com.example.marketplace.service;

import com.example.marketplace.dto.BookingDTO;
import com.example.marketplace.model.Booking;
import com.example.marketplace.model.Payment;
import com.example.marketplace.model.Trip;
import com.example.marketplace.repository.BookingRepository;
import com.example.marketplace.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;
    private final UserService userService;
    private final PaymentService paymentService;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          TripRepository tripRepository,
                          UserService userService,
                          PaymentService paymentService) {
        this.bookingRepository = bookingRepository;
        this.tripRepository = tripRepository;
        this.userService = userService;
        this.paymentService = paymentService;
    }

    // Создание бронирования и соотвествующего платежа
    @Transactional
    public Booking createBooking(BookingDTO bookingDTO) {

        if (bookingDTO.getTripIds() == null || bookingDTO.getTripIds().isEmpty()) {
            throw new IllegalArgumentException("Trip list cannot be empty.");
        }

        List<Trip> trips = tripRepository.findAllById(bookingDTO.getTripIds());
        BigDecimal amount = BigDecimal.ZERO;
        // Проверяем доступные места для каждой поездки
        for (Trip trip : trips) {
            int bookedSeats = bookingRepository.countByTripsContainsAndStatus(trip, Booking.BookingStatus.CONFIRMED);
            amount = amount.add(trip.getPrice());
            if (bookedSeats >= trip.getSeatsAvailable()) {
                throw new IllegalStateException("Not enough seats available for trip with id=%S".formatted(trip.getId()));
            }
        }
        Booking booking = Booking.builder()
                .user(userService.getUserById(bookingDTO.getUserId()))
                .trips(trips)
                .build();
        bookingRepository.save(booking);
        paymentService.createPayment(booking, amount);  // создаем платеж при оформлении брони
        return booking;
    }

    // Отмена брони
    @Transactional
    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id=%s".formatted(id)));

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        Payment payment = paymentService.findByBookingId(id);

        if (payment != null) {
            switch (payment.getStatus()) {
                case PENDING -> payment.setStatus(Payment.PaymentStatus.FAILED);
                case PAID -> paymentService.refundPayment(payment.getId());
                case REFUNDED, FAILED -> {}
                default -> throw new IllegalStateException("Unexpected payment status: %s".formatted(payment.getStatus()));
            }
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
    }

    // Получение всех броней пользователя
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findAllByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("No bookings found for user with id=%s".formatted(userId)));
    }

    // Получение брони по id
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id=%s".formatted(id)));
    }
}