package dev.mateusneres.pixpayviewbackend.dtos.response;

import dev.mateusneres.pixpayviewbackend.enums.TransactionStatus;
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
