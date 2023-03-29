package dev.mateusneres.pixpayviewbackend.services;

import dev.mateusneres.pixpayviewbackend.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    
}
