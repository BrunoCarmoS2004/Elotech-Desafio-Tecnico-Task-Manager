package br.com.elotech.desafio.backend.taskmanager.domain.models;

import br.com.elotech.desafio.backend.taskmanager.domain.Embedded.CommonData;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.Role;
import br.com.elotech.desafio.backend.taskmanager.security.responses.TokenResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Embedded
    private CommonData commonData = new CommonData();

    @Transient
    private TokenResponse tokenResponse;

}