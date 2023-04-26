package dev.mateusneres.pixpayview.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TransactionCreatorRequest {

    @NotNull
    private double price;

}
