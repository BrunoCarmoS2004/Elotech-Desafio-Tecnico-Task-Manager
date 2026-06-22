package br.com.elotech.desafio.backend.taskmanager.services;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.ProjectGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.ProjectPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.puts.ProjectPutDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.models.Project;
import br.com.elotech.desafio.backend.taskmanager.domain.repositories.ProjectRepository;
import br.com.elotech.desafio.backend.taskmanager.exceptions.NotFoundException;
import br.com.elotech.desafio.backend.taskmanager.mappers.ProjectMapper;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import br.com.elotech.desafio.backend.taskmanager.validation.ProjectValidation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private UserService userService;

    @Mock
    private ProjectValidation projectValidation;

    @Mock
    private MessageUtils messageUtils;

    @InjectMocks
    private ProjectService projectService;

    @Test
    @DisplayName("Deve retornar modelo paginado de todos os projetos")
    void getAll_ReturnsPagedModel() {
        Pageable pageable = PageRequest.of(0, 10);
        Project projectMock = mock(Project.class);
        ProjectGetDTO dtoMock = mock(ProjectGetDTO.class);
        Page<Project> pageMock = new PageImpl<>(List.of(projectMock));

        when(projectRepository.findAllProjects(pageable)).thenReturn(pageMock);
        when(projectMapper.projectToProjectGetDTO(projectMock)).thenReturn(dtoMock);

        PagedModel<ProjectGetDTO> result = projectService.getAll(pageable);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve retornar modelo paginado de projetos por criador")
    void getAllByCreator_ReturnsPagedModel() {
        UUID creatorId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        Project projectMock = mock(Project.class);
        ProjectGetDTO dtoMock = mock(ProjectGetDTO.class);
        Page<Project> pageMock = new PageImpl<>(List.of(projectMock));

        when(projectRepository.findByCreatorId(creatorId, pageable)).thenReturn(pageMock);
        when(projectMapper.projectToProjectGetDTO(projectMock)).thenReturn(dtoMock);

        PagedModel<ProjectGetDTO> result = projectService.getAllByCreator(creatorId, pageable);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        verify(projectRepository).findByCreatorId(creatorId, pageable);
    }

    @Test
    @DisplayName("Deve retornar modelo paginado de projetos por membro")
    void getAllByMember_ReturnsPagedModel() {
        UUID memberId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        Project projectMock = mock(Project.class);
        ProjectGetDTO dtoMock = mock(ProjectGetDTO.class);
        Page<Project> pageMock = new PageImpl<>(List.of(projectMock));

        when(projectRepository.findDistinctByMembersUserId(memberId, pageable)).thenReturn(pageMock);
        when(projectMapper.projectToProjectGetDTO(projectMock)).thenReturn(dtoMock);

        PagedModel<ProjectGetDTO> result = projectService.getAllByMember(memberId, pageable);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve buscar projeto por ID com sucesso")
    void getProjectById_Success() {
        UUID id = UUID.randomUUID();
        ProjectGetDTO dtoMock = mock(ProjectGetDTO.class);
        when(projectRepository.findById(id, ProjectGetDTO.class)).thenReturn(Optional.of(dtoMock));

        ProjectGetDTO result = projectService.getProjectById(id);

        assertNotNull(result);
        assertEquals(dtoMock, result);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar projeto por ID que não existe")
    void getProjectById_NotFoundException() {
        UUID id = UUID.randomUUID();
        when(projectRepository.findById(id, ProjectGetDTO.class)).thenReturn(Optional.empty());
        when(messageUtils.getMessage("project.not-found")).thenReturn("Projeto não encontrado");

        assertThrows(NotFoundException.class, () -> projectService.getProjectById(id));
    }

    @Test
    @DisplayName("Deve criar um novo projeto e retornar seu DTO")
    void postProject_Success() {
        ProjectPostDTO postDTOMock = mock(ProjectPostDTO.class);
        Project projectMock = mock(Project.class);
        ProjectGetDTO getDTOMock = mock(ProjectGetDTO.class);

        when(postDTOMock.members()).thenReturn(List.of());
        when(projectMock.getId()).thenReturn(UUID.randomUUID());

        when(projectMapper.projectPostDTOToProject(postDTOMock)).thenReturn(projectMock);
        when(projectRepository.save(projectMock)).thenReturn(projectMock);
        when(projectMapper.projectToProjectGetDTO(projectMock)).thenReturn(getDTOMock);

        ProjectGetDTO result = projectService.postProject(postDTOMock);

        assertNotNull(result);
        assertEquals(getDTOMock, result);
    }

    @Test
    @DisplayName("Deve atualizar um projeto existente com sucesso")
    void putProject_Success() {
        UUID id = UUID.randomUUID();
        ProjectPutDTO putDTOMock = mock(ProjectPutDTO.class);
        Project projectMock = mock(Project.class);
        ProjectGetDTO getDTOMock = mock(ProjectGetDTO.class);

        String projectName = "Projketo Teste 123";
        when(putDTOMock.name()).thenReturn(projectName);

        when(projectRepository.findById(id)).thenReturn(Optional.of(projectMock));
        when(projectRepository.save(projectMock)).thenReturn(projectMock);
        when(projectMapper.projectToProjectGetDTO(projectMock)).thenReturn(getDTOMock);

        ProjectGetDTO result = projectService.putProject(id, putDTOMock);

        assertNotNull(result);
        assertEquals(getDTOMock, result);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar um projeto que não existe")
    void putProject_NotFoundException() {
        UUID id = UUID.randomUUID();
        ProjectPutDTO putDTOMock = mock(ProjectPutDTO.class);
        String projectName = "Projeto Teste 123";

        when(putDTOMock.name()).thenReturn(projectName);
        when(projectRepository.findById(id)).thenReturn(Optional.empty());
        when(messageUtils.getMessage("project.not-found")).thenReturn("Projeto não encontrado");

        assertThrows(NotFoundException.class, () -> projectService.putProject(id, putDTOMock));
    }
}