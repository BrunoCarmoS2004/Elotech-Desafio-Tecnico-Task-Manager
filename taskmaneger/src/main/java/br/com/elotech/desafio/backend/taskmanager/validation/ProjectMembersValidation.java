package br.com.elotech.desafio.backend.taskmanager.validation;

import br.com.elotech.desafio.backend.taskmanager.domain.repositories.ProjectMembersRepository;
import br.com.elotech.desafio.backend.taskmanager.exceptions.ValidationException;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import org.springframework.stereotype.Component;

import java.util.List;
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
        if (!projectMembersRepository.existsById(id)) {
            throw new ValidationException(messageUtils.getMessage("project-members.not-found"));
        }
    }

    public void memberAlreadyInProject(UUID projectId, List<UUID> membersId) {
        if (projectMembersRepository.existsByProjectIdAndUserIdIn(projectId, membersId)) {
            throw new ValidationException(messageUtils.getMessage("project-members.already-in-project"));
        }
    }

    public void memberInProject(UUID projectId, UUID memberId) {
        if (!projectMembersRepository.existsByProjectIdAndUserId(projectId, memberId)) {
            throw new ValidationException(messageUtils.getMessage("project-members.not-in-project"));
        }
    }
}

