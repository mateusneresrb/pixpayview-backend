package dev.mateusneres.pixpayview.repositories;

import dev.mateusneres.pixpayview.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

}
