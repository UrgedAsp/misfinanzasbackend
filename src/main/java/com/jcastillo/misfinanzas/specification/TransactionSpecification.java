package com.jcastillo.misfinanzas.specification;

import com.jcastillo.misfinanzas.model.CategoryType;
import com.jcastillo.misfinanzas.model.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionSpecification {

    // * Filtro por fecha inicial
    public static Specification<Transaction> hasStartedDate(LocalDate startDate) {
        return (root, query, criteriaBuilder) ->
                startDate == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("date"), startDate);
    }

    // * Filtro por fecha final
    public static Specification<Transaction> hasEndDate(LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                endDate == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("date"), endDate);
    }

    // * Filtro por id de categoria
    public static Specification<Transaction> hasCategoryId(Long categoryId) {
        return  (root, query, criteriaBuilder) ->
                categoryId == null ? null : criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    // * Filtro por tipo de categoria
    public static Specification<Transaction> hasCategoryType(CategoryType type) {
        return (root, query, criteriaBuilder) ->
                type == null ? null : criteriaBuilder.equal(root.get("category").get("type"),type);
    }
}
