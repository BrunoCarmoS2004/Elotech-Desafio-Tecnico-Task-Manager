package br.com.elotech.desafio.backend.taskmanager.controllers;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.TaskGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.TaskFilterPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.TaskPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.puts.TaskPutDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskPriority;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.TaskStatus;
import br.com.elotech.desafio.backend.taskmanager.responses.ResponsePayload;
import br.com.elotech.desafio.backend.taskmanager.services.TaskService;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static br.com.elotech.desafio.backend.taskmanager.utils.ServiceUtils.createResponse;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;
    private final MessageUtils messageUtils;

    public TaskController(TaskService taskService, MessageUtils messageUtils) {
        this.taskService = taskService;
        this.messageUtils = messageUtils;
    }

    @GetMapping
    public ResponseEntity<PagedModel<TaskGetDTO>> getAll(@PageableDefault(sort = {"commonData.createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload<TaskGetDTO>> getTaskById(@PathVariable UUID id) {
        TaskGetDTO taskGetDTO = taskService.getTaskById(id);
        return createResponse(
                HttpStatus.OK,
                taskGetDTO.id(),
                taskGetDTO,
                messageUtils.getMessage("task.found")
        );
    }

    @PostMapping
    public ResponseEntity<ResponsePayload<TaskGetDTO>> postTask(@RequestBody @Valid TaskPostDTO taskPostDTO) {
        TaskGetDTO taskGetDTO = taskService.postTask(taskPostDTO);
        return createResponse(
                HttpStatus.CREATED,
                taskGetDTO.id(),
                taskGetDTO,
                messageUtils.getMessage("task.created")
        );
    }

    @PostMapping("/filters")
    public ResponseEntity<PagedModel<TaskGetDTO>> getTasksWithFilters(
            @RequestBody @Valid TaskFilterPostDTO taskFilterPostDTO,
            @PageableDefault(sort = {"title"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getTasksWithFilters(taskFilterPostDTO, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePayload<TaskGetDTO>> putTask(
            @PathVariable UUID id,
            @RequestBody @Valid TaskPutDTO taskPutDTO) {
        TaskGetDTO taskGetDTO = taskService.putTask(id, taskPutDTO);
        return createResponse(
                HttpStatus.OK,
                taskGetDTO.id(),
                taskGetDTO,
                messageUtils.getMessage("task.updated")
        );
    }

    @PatchMapping("/change/{id}/status")
    public ResponseEntity<ResponsePayload<String>> changeTaskStatus(
            @PathVariable UUID id,
            @RequestParam TaskStatus status) {

        taskService.changeTaskStatus(id, status);
        return createResponse(
                HttpStatus.OK,
                id,
                status.name(),
                messageUtils.getMessage("task.status.updated", status.name())
        );
    }

    @PatchMapping("/change/{id}/priority")
    public ResponseEntity<ResponsePayload<String>> changeTaskPriority(
            @PathVariable UUID id,
            @RequestParam TaskPriority priority) {

        taskService.changeTaskPriority(id, priority);
        return createResponse(
                HttpStatus.OK,
                id,
                priority.name(),
                messageUtils.getMessage("task.priority.updated", priority.name())
        );
    }

    @PatchMapping("/change/{id}/responsible/{responsibleId}")
    public ResponseEntity<ResponsePayload<UUID>> changeTaskResponsible(
            @PathVariable UUID id,
            @PathVariable UUID responsibleId) {

        taskService.changeTaskResponsible(id, responsibleId);
        return createResponse(
                HttpStatus.OK,
                id,
                responsibleId,
                messageUtils.getMessage("task.responsible.updated", responsibleId.toString())
        );
    }
}
