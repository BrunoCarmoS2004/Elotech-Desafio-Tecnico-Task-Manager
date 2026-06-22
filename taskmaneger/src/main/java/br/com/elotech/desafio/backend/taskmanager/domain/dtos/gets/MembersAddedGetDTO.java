package br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets;

import java.util.List;
import java.util.UUID;

public record MembersAddedGetDTO(
        UUID projectId,
        List<ProjectMembersGetDTO> membersAdded
) {
}
