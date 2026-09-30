package com.jcastillo.misfinanzas.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long id) {
        super("La transacción con id " + id + " no se encontró");
    }
}
