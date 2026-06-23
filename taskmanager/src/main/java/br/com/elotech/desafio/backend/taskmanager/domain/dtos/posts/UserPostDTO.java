package br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts;

import br.com.elotech.desafio.backend.taskmanager.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserPostDTO(
        @NotBlank(message = "O nome é obrigatório e não pode estar em branco.")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
        String name,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O formato do e-mail informado é inválido.")
        @Size(max = 150, message = "O e-mail não pode ultrapassar 150 caracteres.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, max = 64, message = "A senha deve ter entre 8 e 64 caracteres.")
        String password,

        @NotNull(message = "O perfil do usuário é obrigatório.")
        Role role

) {
}
