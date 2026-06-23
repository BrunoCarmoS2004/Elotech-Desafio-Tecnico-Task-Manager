package br.com.elotech.desafio.backend.taskmanager.security.dtos.gets;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.UserGetDTO;
import br.com.elotech.desafio.backend.taskmanager.security.responses.TokenResponse;

public record UserWithTokenGetDTO(
        UserGetDTO user,
        TokenResponse tokenResponse
) {
}
