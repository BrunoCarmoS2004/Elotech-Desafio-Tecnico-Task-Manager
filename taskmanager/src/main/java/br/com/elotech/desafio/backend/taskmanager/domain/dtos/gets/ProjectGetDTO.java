package br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets;

import java.util.List;
import java.util.UUID;

public record ProjectGetDTO(
        UUID id,

        String name,

        String description,

        UUID creatorId,

        List<ProjectMembersGetDTO> members
) {
}
