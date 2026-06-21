package br.com.elotech.desafio.backend.taskmaneger.security.dtos.gets;

import br.com.elotech.desafio.backend.taskmaneger.domain.enums.Role;

import java.util.UUID;

public record UserLoginValidationGetDTO(
        UUID id,
        Role role,
        String password
) {
}
