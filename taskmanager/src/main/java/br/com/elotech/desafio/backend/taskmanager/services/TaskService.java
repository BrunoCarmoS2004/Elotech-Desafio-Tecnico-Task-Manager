package br.com.elotech.desafio.backend.taskmanager.services;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.TaskGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.TaskFilterPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.TaskPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.puts.TaskPutDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.Role;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskPriority;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskStatus;
import br.com.elotech.desafio.backend.taskmanager.domain.models.Task;
import br.com.elotech.desafio.backend.taskmanager.domain.models.TaskLog;
import br.com.elotech.desafio.backend.taskmanager.domain.models.User;
import br.com.elotech.desafio.backend.taskmanager.domain.repositories.TaskRepository;
import br.com.elotech.desafio.backend.taskmanager.domain.specifications.TaskSpecification;
import br.com.elotech.desafio.backend.taskmanager.exceptions.NotFoundException;
import br.com.elotech.desafio.backend.taskmanager.exceptions.ValidationException;
import br.com.elotech.desafio.backend.taskmanager.mappers.TaskMapper;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import br.com.elotech.desafio.backend.taskmanager.validation.TaskValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static br.com.elotech.desafio.backend.taskmanager.utils.ServiceUtils.getRoleFromToken;
import static br.com.elotech.desafio.backend.taskmanager.utils.ServiceUtils.getUserIdFromToken;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final MessageUtils messageUtils;
    private final UserService userService;
    private final ProjectService projectService;
    private final ProjectMembersService projectMembersService;
    private final TaskValidation taskValidation;
    private final TaskLogService taskLogService;


    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, MessageUtils messageUtils, UserService userService, ProjectService projectService, ProjectMembersService projectMembersService, TaskValidation taskValidation, TaskLogService taskLogService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.messageUtils = messageUtils;
        this.userService = userService;
        this.projectService = projectService;
        this.projectMembersService = projectMembersService;
        this.taskValidation = taskValidation;
        this.taskLogService = taskLogService;
    }

    public PagedModel<TaskGetDTO> getAll(Pageable pageable) {
        return new PagedModel<>(taskRepository.findAllTasks(pageable));
    }

    public TaskGetDTO getTaskById(UUID id) {
        return taskRepository.findTaskById(id).orElseThrow(
                () -> new NotFoundException(messageUtils.getMessage("task.not-found"))
        );
    }

    public PagedModel<TaskGetDTO> getTasksWithFilters(TaskFilterPostDTO taskFilterPostDTO, Pageable pageable) {
        Specification<Task> spec = TaskSpecification.filterTasks(taskFilterPostDTO);

        Page<Task> tasks = taskRepository.findAll(spec, pageable);

        return new PagedModel<>(tasks.map(taskMapper::taskToTaskGetDTO));
    }

    public TaskGetDTO postTask(TaskPostDTO taskPostDTO) {
        taskValidations(taskPostDTO.responsibleId(), taskPostDTO.projectId());
        Task task = taskMapper.taskPostDTOToTask(taskPostDTO);
        taskRepository.save(task);
        return taskMapper.taskToTaskGetDTO(task);
    }

    public TaskGetDTO putTask(UUID id, TaskPutDTO taskPutDTO) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException(messageUtils.getMessage("task.not-found"))
        );
        taskMapper.taskPutDTOToTask(taskPutDTO, task);
        taskRepository.save(task);
        return taskMapper.taskToTaskGetDTO(task);
    }

    public void changeTaskStatus(UUID taskId, TaskStatus taskStatus) {
        TaskGetDTO task = getTaskById(taskId);
        validateStatusChange(task, taskStatus);
        taskRepository.changeTaskStatusTo(taskStatus, taskId);
        saveTaskLog(task.id(), "TaskStatus", task.status().name(), taskStatus.name());
    }

    public void changeTaskPriority(UUID id, TaskPriority newTaskPriority) {
        taskExistsById(id);
        TaskPriority oldPriority = taskRepository.findTaskPriorityById(id);
        taskRepository.changeTaskPriorityTo(newTaskPriority, id);
        saveTaskLog(id, "TaskPriority", oldPriority.name(), newTaskPriority.name());
    }

    public void changeTaskResponsible(UUID id, UUID newResponsibleId) {
        taskExistsById(id);
        validateUserExists(newResponsibleId);
        verifyResponsibleWipLimit(newResponsibleId);
        UUID oldResponsibleId = taskRepository.findResponsibleIdById(id);
        taskRepository.changeResponsibleTo(newResponsibleId, id);
        saveTaskLog(id, "Responsible", oldResponsibleId.toString(), newResponsibleId.toString());
    }

    private void taskValidations(UUID responsibleId, UUID projectId){
        projectMembersService.memberInProject(projectId, responsibleId);
        verifyResponsibleWipLimit(responsibleId);
        validateUserExists(responsibleId);
        projectService.validateProjectExists(projectId);
    }

    private void verifyResponsibleWipLimit(UUID responsibleId) {
        taskValidation.verifyResponsibleWipLimit(responsibleId);
    }

    private void saveTaskLog(UUID taskId, String alteredField, String oldValue, String newValue) {
        Task task = taskRepository.getReferenceById(taskId);
        User user = userService.getReferenceById(getUserIdFromToken());
        taskLogService.saveTaskLog(new TaskLog(
                task,
                user,
                alteredField,
                oldValue,
                newValue
        ));
    }

    private void taskExistsById(UUID id) {
        taskValidation.taskExistsById(id);
    }

    private void validateUserExists(UUID responsibleId) {
        userService.validateUserExists(responsibleId);
    }

    private void validateStatusChange(TaskGetDTO task, TaskStatus taskStatus) {
        if (taskStatus == TaskStatus.TODO && task.status() == TaskStatus.DONE) {
            throw new ValidationException(messageUtils.getMessage("task.invalid.status.updated", TaskStatus.TODO.name()));
        }

        if (taskStatus == TaskStatus.DONE && task.priority() == TaskPriority.CRITICAL) {
            Role userRole = getRoleFromToken();
            if (userRole != Role.MANAGER) {
                throw new ValidationException(messageUtils.getMessage("task.invalid.status.updated", TaskStatus.DONE.name()));
            }
        }
    }
}
