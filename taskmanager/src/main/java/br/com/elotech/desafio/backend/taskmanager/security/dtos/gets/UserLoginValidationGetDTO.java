package br.com.elotech.desafio.backend.taskmanager.security.dtos.gets;

import br.com.elotech.desafio.backend.taskmanager.domain.enums.Role;

import java.util.UUID;

public record UserLoginValidationGetDTO(
        UUID id,
        Role role,
        String email,
        String password
) {
}
