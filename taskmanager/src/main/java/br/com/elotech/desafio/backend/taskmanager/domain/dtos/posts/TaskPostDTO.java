package br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts;

import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskPriority;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskPostDTO(
        @NotBlank(message = "O título é obrigatório.")
        @Size(max = 150, message = "O título deve ter no máximo 150 caracteres.")
        String title,

        String description,

        @NotNull(message = "O status da tarefa é obrigatório.")
        TaskStatus status,

        @NotNull(message = "A prioridade da tarefa é obrigatória.")
        TaskPriority priority,

        @NotNull(message = "O prazo (deadline) é obrigatório.")
        @FutureOrPresent(message = "O prazo deve ser uma data presente ou futura.")
        LocalDateTime deadline,

        UUID responsibleId,

        @NotNull(message = "O ID do projeto é obrigatório.")
        UUID projectId
) {
}
