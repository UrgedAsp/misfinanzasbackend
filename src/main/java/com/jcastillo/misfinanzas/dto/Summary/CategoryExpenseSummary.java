package com.jcastillo.misfinanzas.dto.Summary;

import java.math.BigDecimal;

public record CategoryExpenseSummary(
        Long categoryId,
        String categoryName,
        String categoryColor,
        BigDecimal total
) {
}
