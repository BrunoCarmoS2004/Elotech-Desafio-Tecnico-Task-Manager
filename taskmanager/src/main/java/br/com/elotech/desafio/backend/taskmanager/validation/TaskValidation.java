package br.com.elotech.desafio.backend.taskmanager.validation;

import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskStatus;
import br.com.elotech.desafio.backend.taskmanager.domain.repositories.TaskRepository;
import br.com.elotech.desafio.backend.taskmanager.exceptions.ValidationException;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TaskValidation {
    private final TaskRepository taskRepository;
    private final MessageUtils messageUtils;

    public TaskValidation(TaskRepository taskRepository, MessageUtils messageUtils) {
        this.taskRepository = taskRepository;
        this.messageUtils = messageUtils;
    }

    public void taskExistsById(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new ValidationException(messageUtils.getMessage("task.not-found"));
        }
    }

    public void verifyResponsibleWipLimit(UUID responsibleId) {
        if (taskRepository.countByResponsibleIdAndStatus(responsibleId, TaskStatus.IN_PROGRESS) == 5) {
            throw new ValidationException(messageUtils.getMessage("task.to-many-tasks-in-progress"));
        }
    }
}
