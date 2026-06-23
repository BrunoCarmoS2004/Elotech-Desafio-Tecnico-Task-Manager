package br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets;

import br.com.elotech.desafio.backend.taskmanager.domain.enums.EntityStatus;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskPriority;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskGetDTO(
        UUID id,

        String title,

        String description,

        TaskStatus status,

        TaskPriority priority,

        LocalDateTime deadline,

        UUID responsibleId,

        UUID projectId,

        EntityStatus entityStatus,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
