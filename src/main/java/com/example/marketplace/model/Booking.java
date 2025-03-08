package com.example.marketplace.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime bookingTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    public enum BookingStatus {
        CONFIRMED, CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        this.bookingTime = LocalDateTime.now();
        this.status = BookingStatus.CONFIRMED;
    }
}
