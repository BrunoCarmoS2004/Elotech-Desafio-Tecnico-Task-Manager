package br.com.elotech.desafio.backend.taskmanager.controllers;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.ProjectReportGetDTO;
import br.com.elotech.desafio.backend.taskmanager.responses.ResponsePayload;
import br.com.elotech.desafio.backend.taskmanager.services.ProjectReportService;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static br.com.elotech.desafio.backend.taskmanager.utils.ServiceUtils.createResponse;

@RestController
@RequestMapping("/projects")
public class ProjectReportController {

    private final ProjectReportService projectReportService;
    private final MessageUtils messageUtils;

    public ProjectReportController(ProjectReportService projectReportService, MessageUtils messageUtils) {
        this.projectReportService = projectReportService;
        this.messageUtils = messageUtils;
    }

    @GetMapping("/{projectId}/report")
    public ResponseEntity<ResponsePayload<ProjectReportGetDTO>> getProjectReport(@PathVariable UUID projectId) {
        ProjectReportGetDTO reportDTO = projectReportService.getProjectReport(projectId);
        return createResponse(
                HttpStatus.OK,
                projectId,
                reportDTO,
                messageUtils.getMessage("project.report.generated")
        );
    }
}