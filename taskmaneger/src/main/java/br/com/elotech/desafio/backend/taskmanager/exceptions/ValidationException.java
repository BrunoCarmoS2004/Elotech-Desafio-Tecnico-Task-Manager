package br.com.elotech.desafio.backend.taskmanager.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
