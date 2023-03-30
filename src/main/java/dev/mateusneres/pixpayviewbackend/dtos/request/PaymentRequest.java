package dev.mateusneres.pixpayviewbackend.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentRequest {

    private String description;
    private PayerRequest payer;
    private String payment_method_id;
    private double transaction_amount;

}
