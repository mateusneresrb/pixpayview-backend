package dev.mateusneres.pixpayviewbackend.repositories;

import dev.mateusneres.pixpayviewbackend.entities.Transaction;
import dev.mateusneres.pixpayviewbackend.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByStatus(TransactionStatus transactionStatus);

}
