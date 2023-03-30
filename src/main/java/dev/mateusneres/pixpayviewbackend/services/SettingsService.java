package dev.mateusneres.pixpayviewbackend.services;

import dev.mateusneres.pixpayviewbackend.entities.Settings;
import dev.mateusneres.pixpayviewbackend.repositories.SettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final SettingsRepository settingsRepository;

    public ResponseEntity<Object> updatePaymentToken(String token) {
        Settings settings = settingsRepository.findFirst();
        settings.setPaymentToken(token);

        settingsRepository.save(settings);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
