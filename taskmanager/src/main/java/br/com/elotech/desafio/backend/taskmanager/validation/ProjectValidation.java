package br.com.elotech.desafio.backend.taskmanager.validation;

import br.com.elotech.desafio.backend.taskmanager.domain.repositories.ProjectRepository;
import br.com.elotech.desafio.backend.taskmanager.exceptions.ValidationException;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProjectValidation {
    private final ProjectRepository projectRepository;

    private final MessageUtils messageUtils;

    public ProjectValidation(ProjectRepository projectRepository, MessageUtils messageUtils) {
        this.projectRepository = projectRepository;
        this.messageUtils = messageUtils;
    }

    public void projectNameExists(String name) {
        if (projectRepository.existsByName(name)){
            throw new ValidationException(messageUtils.getMessage("project.name.exists"));
        }
    }

    public void projectExistsById(UUID id) {
        if (!projectRepository.existsById(id)){
            throw new ValidationException(messageUtils.getMessage("project.not-found"));
        }
    }
}
