package br.com.elotech.desafio.backend.taskmaneger.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
