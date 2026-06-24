package br.com.elotech.desafio.backend.taskmanager.services.units;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.MembersAddedGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.ProjectMembersGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.ProjectMembersPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.UserProjectStatus;
import br.com.elotech.desafio.backend.taskmanager.domain.repositories.ProjectMembersRepository;
import br.com.elotech.desafio.backend.taskmanager.exceptions.NotFoundException;
import br.com.elotech.desafio.backend.taskmanager.mappers.ProjectMembersMapper;
import br.com.elotech.desafio.backend.taskmanager.services.ProjectMembersService;
import br.com.elotech.desafio.backend.taskmanager.services.ProjectService;
import br.com.elotech.desafio.backend.taskmanager.services.UserService;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import br.com.elotech.desafio.backend.taskmanager.validation.ProjectMembersValidation;
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
class ProjectMembersServiceTest {

    @Mock
    private ProjectMembersRepository projectMembersRepository;

    @Mock
    private ProjectMembersMapper projectMembersMapper;

    @Mock
    private ProjectMembersValidation projectMembersValidation;

    @Mock
    private ProjectService projectService;

    @Mock
    private UserService userService;

    @Mock
    private MessageUtils messageUtils;

    @InjectMocks
    private ProjectMembersService projectMembersService;

    @Test
    @DisplayName("Deve retornar modelo paginado de todos os membros de projetos")
    void getAll_ReturnsPagedModel() {
        Pageable pageable = PageRequest.of(0, 10);
        ProjectMembersGetDTO dtoMock = mock(ProjectMembersGetDTO.class);
        Page<ProjectMembersGetDTO> pageMock = new PageImpl<>(List.of(dtoMock));

        when(projectMembersRepository.findBy(pageable, ProjectMembersGetDTO.class)).thenReturn(pageMock);

        PagedModel<ProjectMembersGetDTO> result = projectMembersService.getAll(pageable);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve retornar modelo paginado de membros por ID do projeto")
    void getAllProjectMembersByProjectId_ReturnsPagedModel() {
        UUID projectId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        ProjectMembersGetDTO dtoMock = mock(ProjectMembersGetDTO.class);
        Page<ProjectMembersGetDTO> pageMock = new PageImpl<>(List.of(dtoMock));

        when(projectMembersRepository.findAllByProjectId(projectId, pageable)).thenReturn(pageMock);

        PagedModel<ProjectMembersGetDTO> result = projectMembersService.getAllProjectMembersByProjectId(projectId, pageable);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve buscar membro do projeto por ID com sucesso")
    void getProjectMembersById_Success() {
        UUID id = UUID.randomUUID();
        ProjectMembersGetDTO dtoMock = mock(ProjectMembersGetDTO.class);
        when(projectMembersRepository.findById(id, ProjectMembersGetDTO.class)).thenReturn(Optional.of(dtoMock));

        ProjectMembersGetDTO result = projectMembersService.getProjectMembersById(id);

        assertNotNull(result);
        assertEquals(dtoMock, result);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar membro de projeto por ID que não existe")
    void getProjectMembersById_ThrowsNotFoundException() {
        UUID id = UUID.randomUUID();
        when(projectMembersRepository.findById(id, ProjectMembersGetDTO.class)).thenReturn(Optional.empty());
        when(messageUtils.getMessage("project-members.not-found")).thenReturn("Membro de projeto não encontrado");

        assertThrows(NotFoundException.class, () -> projectMembersService.getProjectMembersById(id));
    }

    @Test
    @DisplayName("Deve buscar todos as entidades de membros do projeto que o usuário participa com sucesso")
    void getAllProjectMembersByMemberId_Success() {
        UUID memberId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        ProjectMembersGetDTO dtoMock = mock(ProjectMembersGetDTO.class);
        Page<ProjectMembersGetDTO> pageMock = new PageImpl<>(List.of(dtoMock));
        when(projectMembersRepository.findAllByUserId(memberId, pageable)).thenReturn(pageMock);

        PagedModel<ProjectMembersGetDTO> result = projectMembersService.getAllProjectMembersByMemberId(memberId, pageable);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar membro de projeto pelo ID de usuário inexistente")
    void getAllProjectMembersByMemberId_ThrowsNotFoundException() {
        UUID memberId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        ProjectMembersGetDTO dtoMock = mock(ProjectMembersGetDTO.class);
        Page<ProjectMembersGetDTO> pageMock = new PageImpl<>(List.of(dtoMock));
        when(projectMembersRepository.findAllByUserId(memberId, pageable)).thenReturn(pageMock);
        when(messageUtils.getMessage("project-members.not-found")).thenReturn("Membro de projeto não encontrado");

        assertThrows(RuntimeException.class, () -> projectMembersService.getAllProjectMembersByMemberId(memberId, pageable));
    }

    @Test
    @DisplayName("Deve adicionar membros a um projeto e retornar o DTO correspondente")
    void addMembersProject_Success() {
        ProjectMembersPostDTO postDTOMock = mock(ProjectMembersPostDTO.class);
        UUID projectId = UUID.randomUUID();

        when(postDTOMock.projectId()).thenReturn(projectId);

        when(projectMembersRepository.saveAll(anyList())).thenReturn(List.of());

        MembersAddedGetDTO result = projectMembersService.addMembersProject(postDTOMock);

        assertNotNull(result);
        assertEquals(projectId, result.projectId());
    }

    @Test
    @DisplayName("Deve alterar o status do usuário no projeto com sucesso")
    void changeUserProjectStatus_Success() {
        UUID id = UUID.randomUUID();
        UserProjectStatus statusMock = UserProjectStatus.INACTIVE;

        projectMembersService.changeUserProjectStatus(id, statusMock);

        verify(projectMembersValidation).projectMembersExistsById(id);
        verify(projectMembersRepository).changeUserProjectStatus(statusMock, id);
    }
}