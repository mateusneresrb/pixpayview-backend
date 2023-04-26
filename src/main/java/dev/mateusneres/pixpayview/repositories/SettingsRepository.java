package dev.mateusneres.pixpayview.repositories;

import dev.mateusneres.pixpayview.entities.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingsRepository extends JpaRepository<Settings, Long> {

    Optional<Settings> findFirstByOrderByIdAsc();

}
