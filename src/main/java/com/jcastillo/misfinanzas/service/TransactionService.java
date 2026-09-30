package com.jcastillo.misfinanzas.service;

import com.jcastillo.misfinanzas.dto.Transaction.TransactionRequest;
import com.jcastillo.misfinanzas.dto.Transaction.UpdateTransactionRequest;
import com.jcastillo.misfinanzas.exception.CategoryNotFoundException;
import com.jcastillo.misfinanzas.exception.TransactionNotFoundException;
import com.jcastillo.misfinanzas.model.Category;
import com.jcastillo.misfinanzas.model.CategoryType;
import com.jcastillo.misfinanzas.model.Transaction;
import com.jcastillo.misfinanzas.repository.CategoryRepository;
import com.jcastillo.misfinanzas.repository.TransactionRepository;
import com.jcastillo.misfinanzas.specification.TransactionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public Page<Transaction> findAll(LocalDate startDate, LocalDate endDate, CategoryType type, Long categoryId, Pageable pageable) {

        Specification<Transaction> spec =
                Specification.where(TransactionSpecification.hasStartedDate(startDate))
                        .and(TransactionSpecification.hasEndDate(endDate))
                        .and(TransactionSpecification.hasCategoryType(type))
                        .and(TransactionSpecification.hasCategoryId(categoryId));

        return transactionRepository.findAll(spec, pageable);
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public List<Transaction> findAllByCategory(Category category) {
        return transactionRepository.findAllByCategory(category);
    }

    public Transaction save(TransactionRequest request) {

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));

        Transaction transaction = new Transaction(request.amount(), request.date(), request.description(), category);

        return transactionRepository.save(transaction);
    }

    public Transaction update(Long id, UpdateTransactionRequest request) throws TransactionNotFoundException {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        transaction.setAmount(request.amount());
        transaction.setDate(request.date());
        transaction.setDescription(request.description());
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));

        transaction.setCategory(category);

        return transactionRepository.save(transaction);
    }

    public void deleteById(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new TransactionNotFoundException(id);
        }
        transactionRepository.deleteById(id);
    }
}
