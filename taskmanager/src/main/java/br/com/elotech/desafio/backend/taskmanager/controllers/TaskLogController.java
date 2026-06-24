package br.com.elotech.desafio.backend.taskmanager.controllers;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.TaskLogGetDTO;
import br.com.elotech.desafio.backend.taskmanager.services.TaskLogService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/log")
public class TaskLogController {
    private final TaskLogService taskLogService;

    public TaskLogController(TaskLogService taskLogService) {
        this.taskLogService = taskLogService;
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<PagedModel<TaskLogGetDTO>> getAll(@PathVariable UUID id, @PageableDefault(sort = {"commonData.createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(taskLogService.getAllTaskLogsByTaskId(id, pageable));
    }
}
