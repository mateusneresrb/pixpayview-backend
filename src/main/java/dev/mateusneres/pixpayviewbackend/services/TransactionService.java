package dev.mateusneres.pixpayviewbackend.services;

import dev.mateusneres.pixpayviewbackend.dtos.request.TransactionCreatorRequest;
import dev.mateusneres.pixpayviewbackend.dtos.response.TransactionResponse;
import dev.mateusneres.pixpayviewbackend.entities.Transaction;
import dev.mateusneres.pixpayviewbackend.entities.User;
import dev.mateusneres.pixpayviewbackend.enums.TransactionStatus;
import dev.mateusneres.pixpayviewbackend.exceptions.AccountNotExistsException;
import dev.mateusneres.pixpayviewbackend.repositories.TransactionRepository;
import dev.mateusneres.pixpayviewbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final TransactionRepository transactionRepository;

    public ResponseEntity<Object> createTransaction(String userEmail, TransactionCreatorRequest transactionCreatorRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);

        if (optionalUser.isEmpty()) {
            throw new AccountNotExistsException(4000, "Account by email: " + userEmail + " not exists!");
        }

        User user = optionalUser.get();
        String qrCodePixPayment = paymentService.createQRCodePixPayment(user.getName(), transactionCreatorRequest.getPrice());

        Transaction transaction = new Transaction(
                TransactionStatus.WAITING_PAYMENT,
                qrCodePixPayment,
                transactionCreatorRequest.getPrice(),
                new Timestamp(new Date().getTime()),
                new Timestamp(new Date().getTime()),
                user);

        transactionRepository.save(transaction);

        TransactionResponse transactionResponse = new TransactionResponse();
        BeanUtils.copyProperties(transaction, transactionResponse);

        return ResponseEntity.ok().body(transactionResponse);
    }

}
