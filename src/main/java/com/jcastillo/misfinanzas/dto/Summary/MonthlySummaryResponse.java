package com.jcastillo.misfinanzas.dto.Summary;

import java.math.BigDecimal;
import java.util.List;

public record MonthlySummaryResponse(
        Integer month,
        Integer year,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal balance,
        List<CategoryExpenseSummary> expensesByCategory
) {
}

