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
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PaymentService paymentService;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private User adminUser;

    private User regularUser;

    private Transaction transaction1;

    private Transaction transaction2;

    @Before
    public void setUp() {
        adminUser = new User("admin@example.com", "Admin User", Role.ROLE_ADMIN, "password", Timestamp.from(Instant.now()));
        regularUser = new User("user@example.com", "Regular User", Role.ROLE_USER, "password", Timestamp.from(Instant.now()));

        transaction1 = new Transaction(1L, TransactionStatus.FINISHED,
                "00020126332414BR.GOV.BCB.PIX0114+55219952224235204000053039865802BR5912dfjdfjhdfjdj6009dfjdfjdfj62070503***6304E99D",
                55.00, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), adminUser);

        transaction2 = new Transaction(2L, TransactionStatus.WAITING_PAYMENT,
                "00020126360014BR.GOV.BCB.PIX0114+55219952224235204000053039865802BR5912dfjdfjhdfjdj6009dfjdfjdfj62070503***6304E99D",
                20.00, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), regularUser);
    }

    @Test
    public void shouldFindAllTransactionForAdminUser() {
        when(userRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(adminUser));
        when(transactionRepository.findAll()).thenReturn(List.of(transaction1, transaction2));

        List<TransactionResponse> transactionResponses = transactionService.findAllTransactions(adminUser.getEmail());

        assertThat(transactionResponses)
                .extracting("transactionID", "status", "qrcode", "price", "updatedAt", "createdAt")
                .contains(Tuple.tuple(transaction1.getTransactionID(), transaction1.getStatus(), transaction1.getQrcode(), transaction1.getPrice(), transaction1.getUpdatedAt(), transaction1.getCreatedAt()),
                        Tuple.tuple(transaction2.getTransactionID(), transaction2.getStatus(), transaction2.getQrcode(), transaction2.getPrice(), transaction2.getUpdatedAt(), transaction2.getCreatedAt()))
                .hasSize(2);
    }

    @Test
    public void shouldFindAllTransactionForRegularUser() {
        regularUser.setTransactionList(List.of(transaction1, transaction2));
        when(userRepository.findByEmail(regularUser.getEmail())).thenReturn(Optional.of(regularUser));

        List<TransactionResponse> transactionResponses = transactionService.findAllTransactions(regularUser.getEmail());

        assertThat(transactionResponses)
                .extracting("transactionID", "status", "qrcode", "price", "updatedAt", "createdAt")
                .contains(Tuple.tuple(transaction1.getTransactionID(), transaction1.getStatus(), transaction1.getQrcode(), transaction1.getPrice(), transaction1.getUpdatedAt(), transaction1.getCreatedAt()),
                        Tuple.tuple(transaction2.getTransactionID(), transaction2.getStatus(), transaction2.getQrcode(), transaction2.getPrice(), transaction2.getUpdatedAt(), transaction2.getCreatedAt()))
                .hasSize(2);
    }

    @Test
    public void shouldFindAllTransactionsInvalidUser() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(AccountNotExistsException.class, () -> transactionService.findAllTransactions("test@pixpayview.com"));
    }

    @Test
    public void shouldCreateTransaction() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(regularUser));
        when(paymentService.createQRCodePixPayment(regularUser.getName(), transaction2.getPrice())).thenReturn(new PaymentResponse(transaction1.getPaymentID(), transaction1.getQrcode()));

        TransactionCreatorRequest transactionCreatorRequest = new TransactionCreatorRequest();
        transactionCreatorRequest.setPrice(transaction2.getPrice());

        ResponseEntity<Object> transactionResponse = transactionService.createTransaction(regularUser.getEmail(), transactionCreatorRequest);

        verify(transactionRepository).save(any(Transaction.class));
        assertEquals(HttpStatus.OK, transactionResponse.getStatusCode());
        assertNotNull(transactionResponse.getBody());
    }

    @Test
    public void shouldCreateTransactionInvalidUser() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        TransactionCreatorRequest transactionCreatorRequest = new TransactionCreatorRequest();
        transactionCreatorRequest.setPrice(transaction1.getPrice());

        assertThrows(AccountNotExistsException.class, () -> transactionService.createTransaction(regularUser.getEmail(), transactionCreatorRequest));
    }

    @Test
    public void shouldViewTransactionRegularUser() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(regularUser));
        when(transactionRepository.findById(2L)).thenReturn(Optional.of(transaction2));

        TransactionResponse transactionResponse = transactionService.viewTransaction(regularUser.getEmail(), 2L);

        assertThat(transactionResponse).isNotNull()
                .extracting("transactionID", "status", "qrcode", "price", "updatedAt", "createdAt")
                .containsExactly(transaction2.getTransactionID(), transaction2.getStatus(), transaction2.getQrcode(), transaction2.getPrice(), transaction2.getUpdatedAt(), transaction2.getCreatedAt());
    }

    @Test
    public void shouldViewTransactionInvalidUser() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(AccountNotExistsException.class, () -> transactionService.viewTransaction(regularUser.getEmail(), 1L));
    }

    @Test
    public void shouldViewTransactionInvalidTransaction() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(regularUser));
        when(transactionRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotExistsException.class, () -> transactionService.viewTransaction(regularUser.getEmail(), 2L));
    }

    @Test
    public void shouldDeleteTransaction() {
        when(transactionRepository.findById(2L)).thenReturn(Optional.of(transaction2));

        ResponseEntity<Object> transactionResponse = transactionService.deleteTransaction(2L);

        verify(transactionRepository).delete(any(Transaction.class));
        assertEquals(HttpStatus.NO_CONTENT, transactionResponse.getStatusCode());
        assertNull(transactionResponse.getBody());
    }

    @Test
    public void shouldDeleteTransactionInvalidTransaction() {
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TransactionNotExistsException.class, () -> transactionService.deleteTransaction(2L));
    }

    @Test
    public void shouldViewTransactionNotPermission() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(regularUser));
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction1));

        assertThrows(ForbiddenException.class, () -> transactionService.viewTransaction(regularUser.getEmail(), 1L));
    }

}