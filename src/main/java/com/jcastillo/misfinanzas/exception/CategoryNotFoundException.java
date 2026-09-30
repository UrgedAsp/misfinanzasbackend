package com.jcastillo.misfinanzas.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("La categoria con id " + id + " no se encontró");
    }
}
