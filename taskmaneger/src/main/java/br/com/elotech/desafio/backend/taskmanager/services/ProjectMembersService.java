package br.com.elotech.desafio.backend.taskmanager.services;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.MembersAddedGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.ProjectMembersGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.ProjectMembersPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.UserProjectStatus;
import br.com.elotech.desafio.backend.taskmanager.domain.models.Project;
import br.com.elotech.desafio.backend.taskmanager.domain.models.ProjectMembers;
import br.com.elotech.desafio.backend.taskmanager.domain.models.User;
import br.com.elotech.desafio.backend.taskmanager.domain.repositories.ProjectMembersRepository;
import br.com.elotech.desafio.backend.taskmanager.domain.repositories.ProjectRepository;
import br.com.elotech.desafio.backend.taskmanager.exceptions.NotFoundException;
import br.com.elotech.desafio.backend.taskmanager.mappers.ProjectMembersMapper;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import br.com.elotech.desafio.backend.taskmanager.validation.ProjectMembersValidation;
import br.com.elotech.desafio.backend.taskmanager.validation.ProjectValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectMembersService {
    private final ProjectMembersRepository projectMembersRepository;
    private final ProjectMembersMapper projectMembersMapper;
    private final ProjectMembersValidation projectMembersValidation;
    private final ProjectRepository projectRepository;
    private final ProjectValidation projectValidation;
    private final UserService userService;
    private final MessageUtils messageUtils;

    public ProjectMembersService(ProjectMembersRepository projectMembersRepository, ProjectMembersMapper projectMembersMapper, ProjectMembersValidation projectMembersValidation, ProjectRepository projectRepository, ProjectValidation projectValidation, UserService userService, MessageUtils messageUtils) {
        this.projectMembersRepository = projectMembersRepository;
        this.projectMembersMapper = projectMembersMapper;
        this.projectMembersValidation = projectMembersValidation;
        this.projectRepository = projectRepository;
        this.projectValidation = projectValidation;
        this.userService = userService;
        this.messageUtils = messageUtils;
    }

    public PagedModel<ProjectMembersGetDTO> getAll(Pageable pageable) {
        return new PagedModel<>(projectMembersRepository.findBy(pageable, ProjectMembersGetDTO.class));
    }

    public PagedModel<ProjectMembersGetDTO> getAllProjectMembersByProjectId(UUID projectId, Pageable pageable) {
        return new PagedModel<>(projectMembersRepository.findAllByProjectId(projectId, pageable));
    }

    public PagedModel<ProjectMembersGetDTO> getAllProjectMembersByMemberId(UUID memberId, Pageable pageable) {
        return new PagedModel<>(projectMembersRepository.findAllByUserId(memberId, pageable));
    }

    public ProjectMembersGetDTO getProjectMembersById(UUID id) {
        return projectMembersRepository.findById(id, ProjectMembersGetDTO.class).orElseThrow(
                () -> new NotFoundException(messageUtils.getMessage("project-members.not-found"))
        );
    }

    public MembersAddedGetDTO addMembersProject(ProjectMembersPostDTO projectMembersPostDTO) {
        projectMembersValidations(projectMembersPostDTO);
        List<ProjectMembers> members = new ArrayList<>();
        createProjectMembers(projectMembersPostDTO, members);
        projectMembersRepository.saveAll(members);
        return new MembersAddedGetDTO(projectMembersPostDTO.projectId(), members.stream().map(projectMembersMapper::projectMembersToProjectMembersGetDTO).toList());
    }

    public void changeUserProjectStatus(UUID id, UserProjectStatus userProjectStatus) {
        projectMembersValidation.projectMembersExistsById(id);
        projectMembersRepository.changeUserProjectStatus(userProjectStatus, id);
    }

    private void createProjectMembers(ProjectMembersPostDTO projectMembersPostDTO, List<ProjectMembers> members) {
        Project referenceProject = projectRepository.getReferenceById(projectMembersPostDTO.projectId());
        projectMembersPostDTO.memberIds().forEach(member ->{
            User referenceUser = userService.getReferenceById(member);
            ProjectMembers projectMembers = new ProjectMembers(referenceProject, referenceUser);
            members.add(projectMembers);
        });
    }

    private void projectMembersValidations(ProjectMembersPostDTO projectMembersPostDTO) {
        projectValidation.projectExistsById(projectMembersPostDTO.projectId());
        userService.validateUsersExists(projectMembersPostDTO.memberIds());
    }
}
