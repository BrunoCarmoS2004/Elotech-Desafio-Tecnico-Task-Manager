package br.com.elotech.desafio.backend.taskmaneger.domain.dtos.gets;

import br.com.elotech.desafio.backend.taskmaneger.domain.Embedded.CommonData;
import br.com.elotech.desafio.backend.taskmaneger.domain.enums.Role;
import br.com.elotech.desafio.backend.taskmaneger.security.responses.TokenResponse;
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
