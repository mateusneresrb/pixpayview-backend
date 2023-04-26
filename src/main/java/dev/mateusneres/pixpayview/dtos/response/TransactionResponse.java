package dev.mateusneres.pixpayview.dtos.response;

import dev.mateusneres.pixpayview.enums.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class TransactionResponse {

    private long transactionID;
    private TransactionStatus status;
    private String qrcode;
    private double price;
    private Timestamp updatedAt;
    private Timestamp createdAt;

}
