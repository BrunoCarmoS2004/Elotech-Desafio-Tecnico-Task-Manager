package br.com.elotech.desafio.backend.taskmanager.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
