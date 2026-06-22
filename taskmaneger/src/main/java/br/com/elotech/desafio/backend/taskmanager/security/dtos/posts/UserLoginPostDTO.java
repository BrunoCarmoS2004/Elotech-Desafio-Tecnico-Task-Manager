package br.com.elotech.desafio.backend.taskmanager.security.dtos.posts;

public record UserLoginPostDTO(
        String email,
        String password
) {
}
