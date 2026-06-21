package br.com.elotech.desafio.backend.taskmaneger.security.dtos.posts;

public record UserLoginPostDTO(
        String email,
        String password
) {
}
