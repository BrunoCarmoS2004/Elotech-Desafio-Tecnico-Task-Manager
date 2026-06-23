package br.com.elotech.desafio.backend.taskmanager.services;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.ProjectReportGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskPriority;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskStatus;
import br.com.elotech.desafio.backend.taskmanager.domain.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectReportService {
    private final TaskRepository taskRepository;

    public ProjectReportService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public ProjectReportGetDTO getProjectReport(UUID projectId) {
        Map<TaskStatus, Long> byStatus = taskRepository.countTasksByStatusGrouped(projectId)
                .stream()
                .collect(Collectors.toMap(
                        result -> (TaskStatus) result[0],
                        result -> (Long) result[1]
                ));
        Map<TaskPriority, Long> byPriority = taskRepository.countTasksByPriorityGrouped(projectId)
                .stream()
                .collect(Collectors.toMap(
                        result -> (TaskPriority) result[0],
                        result -> (Long) result[1]
                ));
        return new ProjectReportGetDTO(byStatus, byPriority);
    }
}
