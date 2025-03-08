package com.example.marketplace.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDTO {

    @NotNull(message = "User Id cannot be null")
    @Positive(message = "User Id cannot be negative")
    private Long userId;

    @NotNull(message = "Trip Id cannot be null")
    @Positive(message = "Trip Id cannot be negative")
    private Long tripId;
}
