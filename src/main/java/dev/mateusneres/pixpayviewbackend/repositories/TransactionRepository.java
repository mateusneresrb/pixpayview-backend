package dev.mateusneres.pixpayviewbackend.repositories;

import dev.mateusneres.pixpayviewbackend.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
