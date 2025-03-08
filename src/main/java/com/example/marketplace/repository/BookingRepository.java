package com.example.marketplace.repository;

import com.example.marketplace.model.Booking;
import com.example.marketplace.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<List<Booking>> findAllByUserId(Long userId);
    int countByTripAndStatus(Trip trip, Booking.BookingStatus status);
}