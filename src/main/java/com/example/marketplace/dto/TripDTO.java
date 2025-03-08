package com.example.marketplace.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TripDTO {

    @NotNull(message = "Route ID cannot be null")
    @Positive(message = "Route ID must be positive")
    private Long routeId;

    @NotNull(message = "Transport Type ID cannot be null")
    @Positive(message = "Transport Type ID must be positive")
    private Long transportTypeId;

    @NotNull(message = "Departure Time cannot be null")
    @Future(message = "Departure Time must be in the future")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival Time cannot be null")
    @Future(message = "Arrival Time must be in the future")
    private LocalDateTime arrivalTime;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Seats Available cannot be null")
    @Positive(message = "Seats Available must be positive")
    private Integer seatsAvailable;
}
