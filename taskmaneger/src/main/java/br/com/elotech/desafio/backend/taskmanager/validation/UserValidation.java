package br.com.elotech.desafio.backend.taskmanager.validation;

import br.com.elotech.desafio.backend.taskmanager.domain.repositories.UserRepository;
import br.com.elotech.desafio.backend.taskmanager.exceptions.NotFoundException;
import br.com.elotech.desafio.backend.taskmanager.exceptions.UnauthorizedException;
import br.com.elotech.desafio.backend.taskmanager.exceptions.ValidationException;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserValidation {
    private final UserRepository usuarioRepository;

    private final MessageUtils messageUtils;

    private final PasswordEncoder passwordEncoder;

    public UserValidation(UserRepository usuarioRepository, MessageUtils messageUtils, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.messageUtils = messageUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public void passwordEncoderMatches(String loginPassword, String dbUserPassword) {
        if (!passwordEncoder.matches(loginPassword, dbUserPassword)) {
            throw new UnauthorizedException(messageUtils.getMessage("user.invalid-credentials"));
        }
    }

    public void userExistsById(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException(messageUtils.getMessage("user.not-found"));
        }
    }

    public void userExistsByEmail(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new ValidationException(messageUtils.getMessage("user.email.exists"));
        }
    }

    public void usersExistisByIds(List<UUID> ids) {
        if (usuarioRepository.countByIdIn(ids) != ids.size()) {
            throw new NotFoundException(messageUtils.getMessage("users.not-found"));
        }
    }
}
