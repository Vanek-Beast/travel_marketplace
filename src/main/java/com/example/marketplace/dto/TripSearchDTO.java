package com.example.marketplace.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TripSearchDTO {

    private Long routeId;

    private Long transportTypeId;

    private LocalDateTime departureTime;
}
