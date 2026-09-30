package com.jcastillo.misfinanzas.dto.Transaction;

import com.jcastillo.misfinanzas.model.CategoryType;
import com.jcastillo.misfinanzas.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        BigDecimal amount,
        LocalDate date,
        String description,
        Long categoryId,
        String categoryName,
        CategoryType categoryType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getDescription(),
                transaction.getCategory().getId(),
                transaction.getCategory().getName(),
                transaction.getCategory().getType(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }
}
