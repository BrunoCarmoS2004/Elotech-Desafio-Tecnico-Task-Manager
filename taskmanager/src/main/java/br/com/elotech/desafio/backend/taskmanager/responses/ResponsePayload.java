package br.com.elotech.desafio.backend.taskmanager.responses;

import java.util.UUID;

public record ResponsePayload<T>(
        UUID id,
        String message,
        T data
) {
}
