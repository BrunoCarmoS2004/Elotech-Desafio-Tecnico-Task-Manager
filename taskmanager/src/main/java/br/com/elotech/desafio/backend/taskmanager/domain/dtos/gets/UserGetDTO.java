package br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets;

import br.com.elotech.desafio.backend.taskmanager.domain.enums.EntityStatus;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.Role;

import java.util.UUID;

public record UserGetDTO(
        UUID id,

        String name,

        String email,

        Role role,

        EntityStatus entityStatus
) {
}
