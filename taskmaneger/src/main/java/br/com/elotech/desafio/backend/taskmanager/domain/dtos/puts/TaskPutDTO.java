package br.com.elotech.desafio.backend.taskmanager.domain.dtos.puts;

import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskPriority;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskPutDTO(
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
        LocalDateTime deadline

) {
}
