package br.com.elotech.desafio.backend.taskmanager.controllers;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.MembersAddedGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.ProjectMembersGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.ProjectMembersPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.UserProjectStatus;
import br.com.elotech.desafio.backend.taskmanager.responses.ResponsePayload;
import br.com.elotech.desafio.backend.taskmanager.services.ProjectMembersService;
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
@RequestMapping("/members")
public class ProjectMembersController {
    private final ProjectMembersService projectMembersService;
    private final MessageUtils messageUtils;

    public ProjectMembersController(ProjectMembersService projectMembersService, MessageUtils messageUtils) {
        this.projectMembersService = projectMembersService;
        this.messageUtils = messageUtils;
    }

    @GetMapping
    public ResponseEntity<PagedModel<ProjectMembersGetDTO>> getAll(@PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(projectMembersService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload<ProjectMembersGetDTO>> getProjectMembersById(@PathVariable UUID id) {
        ProjectMembersGetDTO memberDTO = projectMembersService.getProjectMembersById(id);
        return createResponse(
                HttpStatus.OK,
                memberDTO.id(),
                memberDTO,
                messageUtils.getMessage("project-members.found")
        );
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<PagedModel<ProjectMembersGetDTO>> getAllProjectMembersByProjectId(@PathVariable UUID projectId, @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(projectMembersService.getAllProjectMembersByProjectId(projectId, pageable));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<ResponsePayload<ProjectMembersGetDTO>> getProjectMembersByMemberId(@PathVariable UUID memberId) {
        ProjectMembersGetDTO memberDTO = projectMembersService.getProjectMembersByMemberId(memberId);
        return createResponse(
                HttpStatus.OK,
                memberDTO.id(),
                memberDTO,
                messageUtils.getMessage("project-members.found")
        );
    }

    @PostMapping("/add")
    public ResponseEntity<ResponsePayload<MembersAddedGetDTO>> addMembersProject(@RequestBody @Valid ProjectMembersPostDTO projectMembersPostDTO) {
        MembersAddedGetDTO membersAddedGetDTO = projectMembersService.addMembersProject(projectMembersPostDTO);
        return createResponse(
                HttpStatus.CREATED,
                membersAddedGetDTO.projectId(),
                membersAddedGetDTO,
                messageUtils.getMessage("project-members.added")
        );
    }

    @PatchMapping("/change/{id}/status")
    public ResponseEntity<ResponsePayload<String>> changeUserProjectStatus(@PathVariable UUID id, @RequestParam UserProjectStatus status) {
        projectMembersService.changeUserProjectStatus(id, status);
        return createResponse(
                HttpStatus.OK,
                id,
                status.name(),
                messageUtils.getMessage("project-members.status-updated", status.name())
        );
    }
}
