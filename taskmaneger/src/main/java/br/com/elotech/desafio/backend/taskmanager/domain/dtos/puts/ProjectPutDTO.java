package br.com.elotech.desafio.backend.taskmanager.domain.dtos.puts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectPutDTO(
        @NotBlank(message = "O nome é obrigatório e não pode estar em branco.")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
        String name,

        @NotBlank(message = "A descrição é obrigatória e não pode estar em branco.")
        String description

) {
}
