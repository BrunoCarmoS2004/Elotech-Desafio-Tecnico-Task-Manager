package br.com.elotech.desafio.backend.taskmanager.controllers;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.ProjectGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.ProjectPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.puts.ProjectPutDTO;
import br.com.elotech.desafio.backend.taskmanager.responses.ResponsePayload;
import br.com.elotech.desafio.backend.taskmanager.services.ProjectService;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;
    private final MessageUtils messageUtils;

    public ProjectController(ProjectService projectService, MessageUtils messageUtils) {
        this.projectService = projectService;
        this.messageUtils = messageUtils;
    }

    @GetMapping
    public ResponseEntity<PagedModel<ProjectGetDTO>> getAll(@PageableDefault(sort = {"name"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getAll(pageable));
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<PagedModel<ProjectGetDTO>> getAllByCreator(
            @PathVariable UUID creatorId,
            @PageableDefault(sort = {"name"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getAllByCreator(creatorId, pageable));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<PagedModel<ProjectGetDTO>> getAllByMember(
            @PathVariable UUID memberId,
            @PageableDefault(sort = {"name"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getAllByMember(memberId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload<ProjectGetDTO>> getProjectById(@PathVariable UUID id) {
        ProjectGetDTO projectGetDTO = projectService.getProjectById(id);
        return createResponse(
                HttpStatus.OK,
                projectGetDTO.id(),
                projectGetDTO,
                messageUtils.getMessage("project.found")
        );
    }

    @PostMapping
    public ResponseEntity<ResponsePayload<ProjectGetDTO>> postProject(@RequestBody @Valid ProjectPostDTO projectPostDTO) {
        ProjectGetDTO projectGetDTO = projectService.postProject(projectPostDTO);
        return createResponse(
                HttpStatus.CREATED,
                projectGetDTO.id(),
                projectGetDTO,
                messageUtils.getMessage("project.created")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePayload<ProjectGetDTO>> putProject(
            @PathVariable UUID id,
            @RequestBody @Valid ProjectPutDTO projectPutDTO) {
        ProjectGetDTO projectGetDTO = projectService.putProject(id, projectPutDTO);
        return createResponse(
                HttpStatus.OK,
                projectGetDTO.id(),
                projectGetDTO,
                messageUtils.getMessage("project.updated")
        );
    }
}
