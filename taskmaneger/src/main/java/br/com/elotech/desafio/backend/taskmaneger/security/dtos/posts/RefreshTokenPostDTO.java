package br.com.elotech.desafio.backend.taskmaneger.security.dtos.posts;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenPostDTO(
        @NotBlank
        String refreshToken
) {
}
