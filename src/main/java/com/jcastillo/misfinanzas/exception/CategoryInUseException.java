package com.jcastillo.misfinanzas.exception;

public class CategoryInUseException extends RuntimeException {
    public CategoryInUseException(Long id) {
        super("La categoria " + id + " esta siendo usada por una transacción");
    }
}
