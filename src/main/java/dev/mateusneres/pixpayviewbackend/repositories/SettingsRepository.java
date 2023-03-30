package dev.mateusneres.pixpayviewbackend.repositories;

import dev.mateusneres.pixpayviewbackend.entities.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {

    Settings findFirst();

    String findFirstPaymentToken();

}
