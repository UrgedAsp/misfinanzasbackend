package com.jcastillo.misfinanzas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "budgets")
@Getter
@Setter
@NoArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @NotNull
    @Min(1) @Max(12)
    @Column(nullable = false)
    private Integer month;

    @NotNull
    @Min(2026) @Max(2100)
    @Column(nullable = false)
    private Integer year;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    public Budget(Category category, Integer month, Integer year, BigDecimal amount) {
        this.category = category;
        this.month = month;
        this.year = year;
        this.amount = amount;
    }
}