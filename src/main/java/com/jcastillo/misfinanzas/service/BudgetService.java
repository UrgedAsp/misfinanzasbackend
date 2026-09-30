package com.jcastillo.misfinanzas.service;

import com.jcastillo.misfinanzas.dto.Budget.BudgetRequest;
import com.jcastillo.misfinanzas.dto.Budget.BudgetResponse;
import com.jcastillo.misfinanzas.dto.Budget.UpdateBudgetRequest;
import com.jcastillo.misfinanzas.exception.BudgetInvalidCategoryType;
import com.jcastillo.misfinanzas.exception.BudgetNotFoundException;
import com.jcastillo.misfinanzas.exception.CategoryNotFoundException;
import com.jcastillo.misfinanzas.model.Budget;
import com.jcastillo.misfinanzas.model.Category;
import com.jcastillo.misfinanzas.model.CategoryType;
import com.jcastillo.misfinanzas.repository.BudgetRepository;
import com.jcastillo.misfinanzas.repository.CategoryRepository;
import com.jcastillo.misfinanzas.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public List<BudgetResponse> findAll() {
        return budgetRepository.findAll()
                .stream()
                .map(budget -> BudgetResponse.from(budget, getSpentAmount(budget)))
                .toList();
    }

    public BudgetResponse findById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));

        return BudgetResponse.from(budget, getSpentAmount(budget));
    }

    public BudgetResponse save(BudgetRequest request) {

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));

        if (category.getType() != CategoryType.EXPENSE) {
            throw new BudgetInvalidCategoryType();
        }

        Budget budget = new Budget(category, request.month(), request.year(), request.amount());

        Budget budgetSaved = budgetRepository.save(budget);

        return BudgetResponse.from(budgetSaved, getSpentAmount(budgetSaved));
    }

    public BudgetResponse update(Long id, UpdateBudgetRequest request) {
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new BudgetNotFoundException(id));
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));

        if (category.getType() != CategoryType.EXPENSE) {
            throw new BudgetInvalidCategoryType();
        }

        budget.setCategory(category);
        budget.setAmount(request.amount());
        budget.setMonth(request.month());
        budget.setYear(request.year());

        Budget budgetUpdated = budgetRepository.save(budget);

        return BudgetResponse.from(budgetUpdated, getSpentAmount(budgetUpdated));
    }

    public void deleteById(Long id) {
        if (!budgetRepository.existsById(id)) {
            throw new BudgetNotFoundException(id);
        }
        budgetRepository.deleteById(id);
    }

    private BigDecimal getSpentAmount(Budget budget) {
        return transactionRepository.
                sumAmountByCategoryIdAndMonthAndYear(
                        budget.getCategory().getId(),
                        budget.getMonth(),
                        budget.getYear()
                );
    }
}
