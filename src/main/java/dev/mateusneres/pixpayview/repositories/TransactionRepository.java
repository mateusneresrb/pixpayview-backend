package dev.mateusneres.pixpayview.repositories;

import dev.mateusneres.pixpayview.entities.Transaction;
import dev.mateusneres.pixpayview.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByStatus(TransactionStatus transactionStatus);

}
