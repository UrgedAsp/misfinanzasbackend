package com.jcastillo.misfinanzas.dto.Budget;

import com.jcastillo.misfinanzas.model.Budget;
import com.jcastillo.misfinanzas.model.Category;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BudgetResponse(
        Long id,
        Long categoryId,
        String categoryName,
        BigDecimal amount,
        Integer month,
        Integer year,
        BigDecimal spentAmount
) {
    public static BudgetResponse from(Budget budget, BigDecimal spentAmount) {
        return new BudgetResponse(
                budget.getId(),
                budget.getCategory().getId(),
                budget.getCategory().getName(),
                budget.getAmount(),
                budget.getMonth(),
                budget.getYear(),
                spentAmount
        );
    }
}
