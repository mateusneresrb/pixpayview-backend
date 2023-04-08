package dev.mateusneres.pixpayviewbackend.dtos.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class PaymentResponse {

    private final long paymentID;
    private final String QRCode;

}
