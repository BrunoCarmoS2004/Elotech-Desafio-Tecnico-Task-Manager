package br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets;

import br.com.elotech.desafio.backend.taskmanager.domain.enums.UserProjectStatus;

import java.util.UUID;

public record ProjectMembersGetDTO(
        UUID id,

        UUID projectId,

        UUID userId,

        UserProjectStatus userProjectStatus
) {
}
