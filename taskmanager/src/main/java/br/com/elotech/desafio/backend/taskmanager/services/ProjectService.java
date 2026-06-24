package br.com.elotech.desafio.backend.taskmanager.services;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.ProjectGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.ProjectMembersPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.ProjectPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.puts.ProjectPutDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.models.Project;
import br.com.elotech.desafio.backend.taskmanager.domain.repositories.ProjectRepository;
import br.com.elotech.desafio.backend.taskmanager.exceptions.NotFoundException;
import br.com.elotech.desafio.backend.taskmanager.mappers.ProjectMapper;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import br.com.elotech.desafio.backend.taskmanager.validation.ProjectValidation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectValidation projectValidation;
    private final UserService userService;
    private final ProjectMembersService projectMembersService;
    private final MessageUtils messageUtils;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper, ProjectValidation projectValidation, UserService userService, ProjectMembersService projectMembersService, MessageUtils messageUtils) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectValidation = projectValidation;
        this.userService = userService;
        this.projectMembersService = projectMembersService;
        this.messageUtils = messageUtils;
    }

    @Cacheable(value = "projectsListCache", key = "'all-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public PagedModel<ProjectGetDTO> getAll(Pageable pageable) {
        Page<Project> projects = projectRepository.findAllProjects(pageable);
        return new PagedModel<>(projects.map(projectMapper::projectToProjectGetDTO));
    }

    @Cacheable(value = "projectsListCache", key = "'creator-' + #creatorId + '-' + #pageable.pageNumber")
    public PagedModel<ProjectGetDTO> getAllByCreator(UUID creatorId, Pageable pageable) {
        Page<Project> projects = projectRepository.findByCreatorId(creatorId, pageable);
        return new PagedModel<>(projects.map(projectMapper::projectToProjectGetDTO));
    }

    @Cacheable(value = "projectsListCache", key = "'member-' + #memberId + '-' + #pageable.pageNumber")
    public PagedModel<ProjectGetDTO> getAllByMember(UUID memberId, Pageable pageable) {
        Page<Project> projects = projectRepository.findDistinctByMembersUserId(memberId, pageable);
        return new PagedModel<>(projects.map(projectMapper::projectToProjectGetDTO));
    }

    @Cacheable(value = "projectCache", key = "#id")
    public ProjectGetDTO getProjectById(UUID id) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new NotFoundException(messageUtils.getMessage("project.not-found"))
        );
        return projectMapper.projectToProjectGetDTO(project);
    }

    @CacheEvict(value = "projectsListCache", allEntries = true)
    public ProjectGetDTO postProject(ProjectPostDTO projectPostDTO) {
        projectValidations(projectPostDTO);
        Project project = projectMapper.projectPostDTOToProject(projectPostDTO);
        projectRepository.save(project);
        createProjectMembers(project.getId(), projectPostDTO.members());
        return projectMapper.projectToProjectGetDTO(project);
    }

    @Caching(evict = {
            @CacheEvict(value = "projectCache", key = "#id"),
            @CacheEvict(value = "projectsListCache", allEntries = true)
    })
    public ProjectGetDTO putProject(UUID id, ProjectPutDTO projectPutDTO) {
        projectValidation.projectNameExists(projectPutDTO.name());
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new NotFoundException(messageUtils.getMessage("project.not-found"))
        );
        projectMapper.projectPutDTOToProject(projectPutDTO, project);
        projectRepository.save(project);
        return projectMapper.projectToProjectGetDTO(project);
    }

    protected void validateProjectExists(UUID id) {
        projectValidation.projectExistsById(id);
    }

    private void projectValidations(ProjectPostDTO projectPostDTO) {
        userService.validateUserExists(projectPostDTO.creatorId());
        projectValidation.projectNameExists(projectPostDTO.name());
    }

    private void createProjectMembers(UUID id, List<UUID> members) {
        if (!members.isEmpty()) {
            projectMembersService.addMembersProject(new ProjectMembersPostDTO(id, members));
        }
    }
}
