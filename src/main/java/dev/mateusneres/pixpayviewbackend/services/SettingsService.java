package dev.mateusneres.pixpayviewbackend.services;

import dev.mateusneres.pixpayviewbackend.dtos.response.TokenResponse;
import dev.mateusneres.pixpayviewbackend.entities.Settings;
import dev.mateusneres.pixpayviewbackend.exceptions.PaymentTokenNotFoundException;
import dev.mateusneres.pixpayviewbackend.repositories.SettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final SettingsRepository settingsRepository;

    public ResponseEntity<Object> findPaymentToken() {
        Optional<Settings> optionalSettings = settingsRepository.findFirstByOrderByIdAsc();

        if (optionalSettings.isEmpty()) {
            throw new PaymentTokenNotFoundException(4001, "The payment token not exists!");
        }

        String paymentToken = optionalSettings.get().getPaymentToken();
        
        return ResponseEntity.ok(new TokenResponse(paymentToken));
    }

    public ResponseEntity<Object> updatePaymentToken(String token) {
        Settings settings = settingsRepository.findFirstByOrderByIdAsc().orElseGet(Settings::new);
        settings.setPaymentToken(token);

        settingsRepository.save(settings);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
