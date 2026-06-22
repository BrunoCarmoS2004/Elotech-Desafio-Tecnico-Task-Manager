package br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets;

import br.com.elotech.desafio.backend.taskmanager.domain.Embedded.CommonData;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.Role;
import br.com.elotech.desafio.backend.taskmanager.security.responses.TokenResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record UserGetDTO(
        UUID id,

        String name,

        String email,

        Role userRole,

        CommonData commonData,

        TokenResponse tokenResponse
) {
}
