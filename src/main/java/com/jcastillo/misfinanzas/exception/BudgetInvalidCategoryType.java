package com.jcastillo.misfinanzas.exception;

public class BudgetInvalidCategoryType extends RuntimeException {
    public BudgetInvalidCategoryType() {
        super("El tipo de categoria no es valido");
    }
}
