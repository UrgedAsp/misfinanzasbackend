package com.jcastillo.misfinanzas.controller;

import com.jcastillo.misfinanzas.dto.Transaction.TransactionRequest;
import com.jcastillo.misfinanzas.dto.Transaction.TransactionResponse;
import com.jcastillo.misfinanzas.dto.Transaction.UpdateTransactionRequest;
import com.jcastillo.misfinanzas.model.CategoryType;
import com.jcastillo.misfinanzas.model.Transaction;
import com.jcastillo.misfinanzas.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody @Valid TransactionRequest transactionRequest) {
        Transaction transactionSaved = transactionService.save(transactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionResponse.from(transactionSaved));
    }

    @GetMapping
    public Page<TransactionResponse> findAll(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) CategoryType type,
            @RequestParam(required = false) Long categoryId,
            Pageable pageable
    ) {
        return transactionService.findAll(startDate, endDate, type, categoryId, pageable).map(TransactionResponse::from);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> findById(@PathVariable Long id) {
        Transaction transaction = transactionService.findById(id);
        return ResponseEntity.ok(TransactionResponse.from(transaction));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateTransactionRequest updateTransactionRequest) {
        Transaction transaction = transactionService.update(id, updateTransactionRequest);
        return ResponseEntity.ok(TransactionResponse.from(transaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
