package dev.mateusneres.pixpayviewbackend.controllers;

import dev.mateusneres.pixpayviewbackend.dtos.request.TransactionCreatorRequest;
import dev.mateusneres.pixpayviewbackend.exceptions.BadRequestException;
import dev.mateusneres.pixpayviewbackend.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(value = "/create")
    public ResponseEntity<Object> createTransaction(@Valid @RequestBody TransactionCreatorRequest transactionCreatorRequest, Authentication authentication, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(5000, "The request sent is invalid, check a application documentation.");
        }

        return transactionService.createTransaction(authentication.getName(), transactionCreatorRequest);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> viewTransaction(Authentication authentication, @PathVariable("id") long transactionID) {
        return ResponseEntity.ok(transactionService.viewTransaction(authentication.getName(), transactionID));
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<Object> deleteTransaction(@PathVariable("id") long transactionID) {
        return transactionService.deleteTransaction(transactionID);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<Object> listTransactions(Authentication authentication) {
        return ResponseEntity.ok(transactionService.findAllTransactions(authentication.getName()));
    }

}
