package dev.mateusneres.pixpayview.services;

import dev.mateusneres.pixpayview.dtos.request.TransactionCreatorRequest;
import dev.mateusneres.pixpayview.dtos.response.PaymentResponse;
import dev.mateusneres.pixpayview.dtos.response.TransactionResponse;
import dev.mateusneres.pixpayview.entities.Transaction;
import dev.mateusneres.pixpayview.entities.User;
import dev.mateusneres.pixpayview.enums.Role;
import dev.mateusneres.pixpayview.enums.TransactionStatus;
import dev.mateusneres.pixpayview.exceptions.AccountNotExistsException;
import dev.mateusneres.pixpayview.exceptions.ForbiddenException;
import dev.mateusneres.pixpayview.exceptions.TransactionNotExistsException;
import dev.mateusneres.pixpayview.repositories.TransactionRepository;
import dev.mateusneres.pixpayview.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final TransactionRepository transactionRepository;

    public List<TransactionResponse> findAllTransactions(String userEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);

        if (optionalUser.isEmpty()) {
            throw new AccountNotExistsException(4000, "Account by email: " + userEmail + " not exists!");
        }

        User user = optionalUser.get();

        if (user.getRole() == Role.ROLE_ADMIN) {
            List<Transaction> allTransactions = transactionRepository.findAll();

            return getTransactionResponse(allTransactions);
        }

        return getTransactionResponse(user.getTransactionList());
    }

    public ResponseEntity<Object> createTransaction(String userEmail, TransactionCreatorRequest transactionCreatorRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);

        if (optionalUser.isEmpty()) {
            throw new AccountNotExistsException(4000, "Account by email: " + userEmail + " not exists!");
        }

        User user = optionalUser.get();
        PaymentResponse paymentResponse = paymentService.createQRCodePixPayment(user.getName(), transactionCreatorRequest.getPrice());

        Transaction transaction = new Transaction(
                paymentResponse.getPaymentID(),
                TransactionStatus.WAITING_PAYMENT,
                paymentResponse.getQRCode(),
                transactionCreatorRequest.getPrice(),
                new Timestamp(new Date().getTime()),
                new Timestamp(new Date().getTime()),
                user);

        transactionRepository.save(transaction);

        TransactionResponse transactionResponse = new TransactionResponse();
        BeanUtils.copyProperties(transaction, transactionResponse);

        return ResponseEntity.ok().body(transactionResponse);
    }

    public TransactionResponse viewTransaction(String userEmail, long transactionID) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionID);

        User user = optionalUser.orElseThrow(() -> new AccountNotExistsException(4000, "The user account not exists"));
        Transaction transaction = optionalTransaction.orElseThrow(() -> new TransactionNotExistsException(4002, "The transaction by id " + transactionID + " not exists!"));

        if (!transaction.getUser().equals(user) && optionalUser.get().getRole() != Role.ROLE_ADMIN) {
            throw new ForbiddenException(7000, "You don't have enough permissions to change another user's settings.");
        }

        TransactionResponse transactionResponse = new TransactionResponse();
        BeanUtils.copyProperties(transaction, transactionResponse);

        return transactionResponse;
    }

    public ResponseEntity<Object> deleteTransaction(long transactionID) {
        Transaction transaction = transactionRepository.findById(transactionID).orElseThrow(() -> new TransactionNotExistsException(4002, "Transaction by id: " + transactionID + " not exists!"));

        transactionRepository.delete(transaction);
        return ResponseEntity.status(204).build();
    }

    private List<TransactionResponse> getTransactionResponse(List<Transaction> transactions) {
        List<TransactionResponse> transactionResponses = new ArrayList<>();

        transactions.forEach(transaction -> {
            TransactionResponse transactionResponse = new TransactionResponse();
            BeanUtils.copyProperties(transaction, transactionResponse);
            transactionResponses.add(transactionResponse);
        });

        return transactionResponses;
    }

}
