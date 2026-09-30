package com.jcastillo.misfinanzas.repository;

import com.jcastillo.misfinanzas.dto.Summary.CategoryExpenseSummary;
import com.jcastillo.misfinanzas.model.Category;
import com.jcastillo.misfinanzas.model.CategoryType;
import com.jcastillo.misfinanzas.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    List<Transaction> findAllByCategory(Category category);

    boolean existsByCategoryId(Long id);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.category.id = :categoryId
            AND EXTRACT(MONTH FROM t.date) = :month
            AND EXTRACT(YEAR FROM t.date) = :year
            """)
    BigDecimal sumAmountByCategoryIdAndMonthAndYear(
            @Param("categoryId") Long categoryId,
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    @Query("""
                SELECT COALESCE(SUM(t.amount), 0)
                FROM Transaction t
                WHERE t.category.type = :type
                  AND EXTRACT(MONTH FROM t.date) = :month
                  AND EXTRACT(YEAR FROM t.date) = :year
            """)
    BigDecimal sumAmountByTypeAndMonthAndYear(
            @Param("type") CategoryType type,
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    @Query("""
                SELECT new com.jcastillo.misfinanzas.dto.Summary.CategoryExpenseSummary(
                    t.category.id,
                    t.category.name,
                    t.category.color,
                    SUM(t.amount)
                )
                FROM Transaction t
                WHERE t.category.type = 'EXPENSE'
                  AND EXTRACT(MONTH FROM t.date) = :month
                  AND EXTRACT(YEAR FROM t.date) = :year
                GROUP BY t.category.id, t.category.name, t.category.color
                ORDER BY SUM(t.amount) DESC
            """)
    List<CategoryExpenseSummary> findExpenseSummaryByCategory(
            @Param("month") Integer month,
            @Param("year") Integer year
    );
}
