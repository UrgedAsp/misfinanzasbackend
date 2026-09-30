package com.jcastillo.misfinanzas.repository;

import com.jcastillo.misfinanzas.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

}
