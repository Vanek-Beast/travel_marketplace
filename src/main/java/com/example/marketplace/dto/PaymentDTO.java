package com.example.marketplace.dto;

import com.example.marketplace.model.Payment;
 import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {

    private Payment.PaymentMethod paymentMethod;
}
