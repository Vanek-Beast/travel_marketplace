package com.example.marketplace.dto;

import com.example.marketplace.model.Payment;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {

    @NotBlank(message = "Payment method is required")
    private Payment.PaymentMethod paymentMethod;
}
