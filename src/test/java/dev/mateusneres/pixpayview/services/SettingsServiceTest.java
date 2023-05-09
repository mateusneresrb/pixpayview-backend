package dev.mateusneres.pixpayview.services;

import dev.mateusneres.pixpayview.dtos.response.TokenResponse;
import dev.mateusneres.pixpayview.entities.Settings;
import dev.mateusneres.pixpayview.exceptions.PaymentTokenNotFoundException;
import dev.mateusneres.pixpayview.repositories.SettingsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SettingsServiceTest {

    private Settings settings;

    @Mock
    private SettingsRepository settingsRepository;

    @InjectMocks
    private SettingsService settingsService;

    @Before
    public void setUp(){
        settings = new Settings();
        settings.setId(0L);
        settings.setPaymentToken("345345fdshdfjqw3r4t5y6u7i8o9p0oiuytrewqasdfghjklmnbvcxz");
    }

    @Test
    @DisplayName("Test findPaymentToken")
    public void shouldFindPaymentToken() {
        when(settingsRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(settings));
        ResponseEntity<Object> result = settingsService.findPaymentToken();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("345345fdshdfjqw3r4t5y6u7i8o9p0oiuytrewqasdfghjklmnbvcxz", ((TokenResponse) result.getBody()).getPaymentToken());
    }

    @Test
    @DisplayName("Test findPaymentToken is token empty")
    public void shouldThrowPaymentTokenNotFoundException() {
        when(settingsRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.empty());

        assertThrows(PaymentTokenNotFoundException.class, () -> settingsService.findPaymentToken());
    }

    @Test
    @DisplayName("Test updatePaymentToken")
    public void shouldUpdatePaymentToken() {
        when(settingsRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(settings));

        ResponseEntity<Object> result = settingsService.updatePaymentToken("345345fdshdfjqw3r4t5y6u7i8o9p0oiuytrewqasdfghjklmnbvcxz");
        verify(settingsRepository).save(any(Settings.class));

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    @DisplayName("Test updatePaymentToken when new settings")
    public void shouldUpdatePaymentTokenWhenNewSettings() {
        when(settingsRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.empty());

        ResponseEntity<Object> result = settingsService.updatePaymentToken("345345fdshdfjqw3r4t5y6u7i8o9p0oiuytrewqasdfghjklmnbvcxz");
        verify(settingsRepository).save(any(Settings.class));

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }
}
