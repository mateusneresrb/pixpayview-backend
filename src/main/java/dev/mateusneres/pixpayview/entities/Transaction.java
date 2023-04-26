package dev.mateusneres.pixpayview.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.mateusneres.pixpayview.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "transactions")

@Getter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private long transactionID;

    @Column(nullable = false)
    private long paymentID;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(nullable = false)
    private String qrcode;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private Timestamp updatedAt;

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Transaction(long paymentID, TransactionStatus status, String qrcode, double price, Timestamp updatedAt, Timestamp createdAt, User user) {
        this.paymentID = paymentID;
        this.status = status;
        this.qrcode = qrcode;
        this.price = price;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.user = user;
    }

}
