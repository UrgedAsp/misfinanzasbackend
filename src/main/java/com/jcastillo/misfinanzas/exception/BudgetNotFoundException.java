package com.jcastillo.misfinanzas.exception;

public class BudgetNotFoundException extends RuntimeException {
    public BudgetNotFoundException(Long id) {
        super("El budget con id " + id + " no se encontró");
    }
}
