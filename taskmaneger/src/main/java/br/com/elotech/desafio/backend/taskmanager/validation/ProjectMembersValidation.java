package br.com.elotech.desafio.backend.taskmanager.validation;

import br.com.elotech.desafio.backend.taskmanager.domain.repositories.ProjectMembersRepository;
import br.com.elotech.desafio.backend.taskmanager.exceptions.ValidationException;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProjectMembersValidation {
    private final ProjectMembersRepository projectMembersRepository;
    private final MessageUtils messageUtils;

    public ProjectMembersValidation(ProjectMembersRepository projectMembersRepository, MessageUtils messageUtils) {
        this.projectMembersRepository = projectMembersRepository;
        this.messageUtils = messageUtils;
    }

    public void projectMembersExistsById(UUID id) {
        if (!projectMembersRepository.existsById(id)){
            throw new ValidationException(messageUtils.getMessage("project.not-found"));
        }
    }
}

