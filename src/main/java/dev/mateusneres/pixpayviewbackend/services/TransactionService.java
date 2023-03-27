package dev.mateusneres.pixpayviewbackend.services;

import dev.mateusneres.pixpayviewbackend.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;
    
}
