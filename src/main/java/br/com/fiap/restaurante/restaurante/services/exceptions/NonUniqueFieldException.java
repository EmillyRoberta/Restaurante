package br.com.fiap.restaurante.restaurante.services.exceptions;

public class NonUniqueFieldException extends RuntimeException {
    public NonUniqueFieldException(String message) {
        super(message);
    }
}
