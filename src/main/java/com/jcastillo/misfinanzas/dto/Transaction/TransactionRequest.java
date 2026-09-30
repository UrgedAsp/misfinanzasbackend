package com.jcastillo.misfinanzas.dto.Transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        @NotNull @Positive BigDecimal amount,
        @NotNull @PastOrPresent LocalDate date,
        @Size(max = 150) String description,
        @NotNull Long categoryId
) {
}
