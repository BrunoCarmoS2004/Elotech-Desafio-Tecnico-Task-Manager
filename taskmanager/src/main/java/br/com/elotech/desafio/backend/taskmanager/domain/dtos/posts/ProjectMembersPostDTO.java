package br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ProjectMembersPostDTO(
        @NotNull(message = "O id do projeto é obrigatório.")
        UUID projectId,
        @NotNull(message = "A lista de membros a serem adicionados deve estar no mínimo vazia.")
        List<UUID> memberIds
) {
}
