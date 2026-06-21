package br.com.elotech.desafio.backend.taskmaneger.security.responses;

public record TokenResponse(
        String token,
        String refreshToken
) {
}
