package com.jcastillo.misfinanzas.dto.Budget;

import com.jcastillo.misfinanzas.model.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateBudgetRequest(
        @NotNull Long categoryId,
        @NotNull @Positive BigDecimal amount,
        @NotNull @Min(1) @Max(12) Integer month,
        @NotNull @Min(2026) @Max(2100) Integer year
) {
}
