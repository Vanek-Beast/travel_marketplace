package com.example.marketplace.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookingDTO {

    private Long userId;

    private List<Long> tripIds;
}
