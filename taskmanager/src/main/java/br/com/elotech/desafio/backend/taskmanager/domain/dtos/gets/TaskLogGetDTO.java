package br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskLogGetDTO(
        UUID id,
        UUID taskId,
        UUID userId,
        String alteredField,
        String oldValue,
        String newValue,
        LocalDateTime alteredDate
) {
}
