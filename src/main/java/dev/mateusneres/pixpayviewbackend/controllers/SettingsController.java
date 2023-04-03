package dev.mateusneres.pixpayviewbackend.controllers;

import dev.mateusneres.pixpayviewbackend.dtos.request.SettingsRequest;
import dev.mateusneres.pixpayviewbackend.exceptions.BadRequestException;
import dev.mateusneres.pixpayviewbackend.services.SettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/settings")

@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;

    @GetMapping("/token")
    public ResponseEntity<Object> getPaymentToken() {
        return settingsService.findPaymentToken();
    }

    @PutMapping("/token/update")
    public ResponseEntity<Object> updatePaymentToken(@Valid @RequestBody SettingsRequest settingsRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(5000, "The request sent is invalid, check a application documentation.");
        }

        return settingsService.updatePaymentToken(settingsRequest.getPaymentToken());
    }

}
