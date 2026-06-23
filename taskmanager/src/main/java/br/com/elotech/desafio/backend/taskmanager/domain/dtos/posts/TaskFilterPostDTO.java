package br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts;

import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskPriority;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskFilterPostDTO(
        TaskStatus status,
        TaskPriority priority,
        UUID responsibleId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String searchText
) {
}
