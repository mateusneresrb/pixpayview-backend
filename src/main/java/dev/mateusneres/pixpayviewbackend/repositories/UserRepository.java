package dev.mateusneres.pixpayviewbackend.repositories;

import dev.mateusneres.pixpayviewbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User getUserByEmail(String email);

    boolean existsUserByEmail(String email);

}
