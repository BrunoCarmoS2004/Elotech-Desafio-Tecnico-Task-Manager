package br.com.elotech.desafio.backend.taskmanager.security.responses;

public record TokenResponse(
        String token,
        String refreshToken
) {
}
