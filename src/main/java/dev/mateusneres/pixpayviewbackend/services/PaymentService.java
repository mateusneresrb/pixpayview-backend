package dev.mateusneres.pixpayviewbackend.services;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import dev.mateusneres.pixpayviewbackend.dtos.response.PaymentResponse;
import dev.mateusneres.pixpayviewbackend.entities.Settings;
import dev.mateusneres.pixpayviewbackend.entities.Transaction;
import dev.mateusneres.pixpayviewbackend.enums.TransactionStatus;
import dev.mateusneres.pixpayviewbackend.exceptions.PaymentTokenNotFoundException;
import dev.mateusneres.pixpayviewbackend.repositories.SettingsRepository;
import dev.mateusneres.pixpayviewbackend.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class PaymentService {

    @Value(("${payment.qrcode.expires}"))
    private int timeQRCodeExpires;

    private final String PAYMENT_STATUS_APPROVED = "approved";

    private final SettingsRepository settingsRepository;
    private final TransactionRepository transactionRepository;

    public PaymentResponse createQRCodePixPayment(String name, double price) {
        Optional<Settings> optionalSettings = settingsRepository.findFirstByOrderByIdAsc();

        if (optionalSettings.isEmpty()) {
            throw new PaymentTokenNotFoundException(4001, "The payment token not exists!");
        }

        String PAYMENT_TOKEN = optionalSettings.get().getPaymentToken();

        try {
            MercadoPagoConfig.setAccessToken(PAYMENT_TOKEN);

            PaymentCreateRequest createRequest =
                    PaymentCreateRequest.builder()
                            .transactionAmount(new BigDecimal(price))
                            .description("Generated by PixPayView!")
                            .paymentMethodId("pix")
                            .dateOfExpiration(OffsetDateTime.now().plusMinutes(timeQRCodeExpires))
                            .payer(PaymentPayerRequest.builder().email("auto@pixpayview.com").build())
                            .build();

            PaymentClient paymentClient = new PaymentClient();
            Payment createdPayment = paymentClient.create(createRequest);

            long paymentID = createdPayment.getId();
            String qrCode = createdPayment.getPointOfInteraction().getTransactionData().getQrCode();

            return new PaymentResponse(paymentID, qrCode);
        } catch (MPApiException ex) {
            System.out.printf(
                    "MercadoPago Error. Status: %s, Content: %s%n",
                    ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
        } catch (MPException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Scheduled(fixedRate = 1000 * 30)
    public void scheduleCheckTransactions() {
        List<Transaction> waitingPaymentTransactions = transactionRepository.findAllByStatus(TransactionStatus.WAITING_PAYMENT);
        List<Transaction> updatedTransactions = Lists.newArrayList();

        updatedTransactions.addAll(getListExpiredQRCode(waitingPaymentTransactions));
        waitingPaymentTransactions.removeAll(updatedTransactions);

        updatedTransactions.addAll(getFinishedTransactions(waitingPaymentTransactions));

        transactionRepository.saveAll(updatedTransactions);
    }

    private List<Transaction> getFinishedTransactions(List<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> isPaymentFinished(transaction.getPaymentID()))
                .peek(transaction -> transaction.setStatus(TransactionStatus.FINISHED))
                .toList();
    }

    private List<Transaction> getListExpiredQRCode(List<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> Duration.between(transaction.getCreatedAt().toInstant(), Instant.now()).toMinutes() >= (timeQRCodeExpires + 10))
                .peek(transaction -> transaction.setStatus(TransactionStatus.EXPIRED_QRCODE))
                .toList();
    }

    private boolean isPaymentFinished(long paymentID) {
        Optional<Settings> optionalSettings = settingsRepository.findFirstByOrderByIdAsc();
        Settings settings = optionalSettings.orElseThrow(() -> new PaymentTokenNotFoundException(4001, "The payment token not exists!"));

        String PAYMENT_TOKEN = settings.getPaymentToken();

        try {
            MercadoPagoConfig.setAccessToken(PAYMENT_TOKEN);
            PaymentClient paymentClient = new PaymentClient();

            Payment payment = paymentClient.get(paymentID);
            return payment.getStatus().equalsIgnoreCase(PAYMENT_STATUS_APPROVED);
        } catch (MPApiException ex) {
            System.out.printf(
                    "MercadoPago Error. Status: %s, Content: %s%n",
                    ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
        } catch (
                MPException ex) {
            ex.printStackTrace();
        }

        return false;
    }

}