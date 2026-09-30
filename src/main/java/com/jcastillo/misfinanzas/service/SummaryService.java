package com.jcastillo.misfinanzas.service;

import com.jcastillo.misfinanzas.dto.Summary.CategoryExpenseSummary;
import com.jcastillo.misfinanzas.dto.Summary.MonthlySummaryResponse;
import com.jcastillo.misfinanzas.model.CategoryType;
import com.jcastillo.misfinanzas.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final TransactionRepository transactionRepository;

    public MonthlySummaryResponse monthlySummary (Integer month, Integer year) {
        BigDecimal totalIncome = transactionRepository.sumAmountByTypeAndMonthAndYear(CategoryType.INCOME, month, year);
        BigDecimal totalExpense = transactionRepository.sumAmountByTypeAndMonthAndYear(CategoryType.EXPENSE, month, year);
        BigDecimal balance = totalIncome.subtract(totalExpense);
        List<CategoryExpenseSummary> categoryExpenseSummaries = transactionRepository.findExpenseSummaryByCategory(month, year);
        return new MonthlySummaryResponse(month, year, totalIncome, totalExpense, balance, categoryExpenseSummaries);
    }

}
