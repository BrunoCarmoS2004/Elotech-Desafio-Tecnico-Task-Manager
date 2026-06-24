package br.com.elotech.desafio.backend.taskmanager.services.units;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.UserGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.UserPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.EntityStatus;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.Role;
import br.com.elotech.desafio.backend.taskmanager.domain.models.User;
import br.com.elotech.desafio.backend.taskmanager.domain.repositories.UserRepository;
import br.com.elotech.desafio.backend.taskmanager.exceptions.NotFoundException;
import br.com.elotech.desafio.backend.taskmanager.exceptions.UnauthorizedException;
import br.com.elotech.desafio.backend.taskmanager.mappers.UserMapper;
import br.com.elotech.desafio.backend.taskmanager.security.dtos.gets.UserLoginValidationGetDTO;
import br.com.elotech.desafio.backend.taskmanager.security.dtos.gets.UserWithTokenGetDTO;
import br.com.elotech.desafio.backend.taskmanager.security.services.TokenService;
import br.com.elotech.desafio.backend.taskmanager.services.UserService;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import br.com.elotech.desafio.backend.taskmanager.validation.UserValidation;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserValidation userValidation;

    @Mock
    private MessageUtils messageUtils;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve validar o login com sucesso quando credenciais forem corretas")
    void validateLogin_Success() {
        String email = "teste@elotech.com";
        String password = "senha123123";
        UserLoginValidationGetDTO dtoMock = mock(UserLoginValidationGetDTO.class);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(dtoMock));
        when(dtoMock.password()).thenReturn("senhaCodificada");

        UserLoginValidationGetDTO result = userService.validateLogin(email, password);

        assertNotNull(result);
        assertEquals(dtoMock, result);
        verify(userValidation).passwordEncoderMatches(password, "senhaCodificada");
    }


    @Test
    @DisplayName("Deve lançar exceção ao tentar validar login com e-mail que não existe")
    void validateLogin_Unauthorized() {
        String email = "teste@elotech.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(messageUtils.getMessage("user.invalid-credentials")).thenReturn("Credenciais inválidas");

        assertThrows(UnauthorizedException.class, () -> userService.validateLogin(email, "senha123123"));
    }

    @Test
    @DisplayName("Deve validar o refresh token com sucesso")
    void validateRefresh_Success() {
        UUID id = UUID.randomUUID();
        UserLoginValidationGetDTO dtoMock = mock(UserLoginValidationGetDTO.class);
        when(usuarioRepository.findById(id, UserLoginValidationGetDTO.class)).thenReturn(Optional.of(dtoMock));

        UserLoginValidationGetDTO result = userService.validateRefresh(id);

        assertNotNull(result);
        assertEquals(dtoMock, result);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar o refresh token com e-mail que não existe")
    void validateRefresh_Unauthorized() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.findById(id, UserLoginValidationGetDTO.class)).thenReturn(Optional.empty());
        when(messageUtils.getMessage("user.invalid-credentials")).thenReturn("Credenciais inválidas");

        assertThrows(UnauthorizedException.class, () -> userService.validateRefresh(id));
    }

    @Test
    @DisplayName("Deve retornar modelo paginado de usuários")
    void getAll_ReturnsPagedModel() {
        Pageable pageable = PageRequest.of(0, 10);
        UserGetDTO dtoMock = mock(UserGetDTO.class);
        Page<UserGetDTO> pageMock = new PageImpl<>(List.of(dtoMock));

        when(usuarioRepository.findBy(pageable, UserGetDTO.class)).thenReturn(pageMock);

        PagedModel<UserGetDTO> result = userService.getAll(pageable);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void getUserById_Success() {
        UUID id = UUID.randomUUID();
        UserGetDTO dtoMock = mock(UserGetDTO.class);
        when(usuarioRepository.findById(id, UserGetDTO.class)).thenReturn(Optional.of(dtoMock));

        UserGetDTO result = userService.getUserById(id);

        assertNotNull(result);
        assertEquals(dtoMock, result);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário por ID que não existe")
    void getUserById_NotFoundException() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.findById(id, UserGetDTO.class)).thenReturn(Optional.empty());
        when(messageUtils.getMessage("user.not-found")).thenReturn("Usuário não encontrado");

        assertThrows(NotFoundException.class, () -> userService.getUserById(id));
    }

    @Test
    @DisplayName("Deve criar um novo usuário e retornar seu DTO")
    void postUser_Success() {
        UserPostDTO postDTOMock = mock(UserPostDTO.class);
        when(postDTOMock.email()).thenReturn("teste@elotech.com");
        when(postDTOMock.password()).thenReturn("senhaAberta");

        User userMock = mock(User.class);
        when(userMock.getId()).thenReturn(UUID.randomUUID());
        when(userMock.getPassword()).thenReturn("senhaCodificada");

        UserWithTokenGetDTO expectedReturnDTO = mock(UserWithTokenGetDTO.class);

        when(userMapper.userPostDTOToUser(postDTOMock)).thenReturn(userMock);
        when(passwordEncoder.encode("senhaAberta")).thenReturn("senhaCodificada");
        when(usuarioRepository.save(userMock)).thenReturn(userMock);

        when(tokenService.generateTokenResponse(any())).thenReturn(null);
        when(userMapper.userToUserWithTokenGetDTO(userMock)).thenReturn(expectedReturnDTO);

        UserWithTokenGetDTO result = userService.postUser(postDTOMock);

        assertNotNull(result);
        assertEquals(expectedReturnDTO, result);
    }

    @Test
    @DisplayName("Deve alterar o nome do usuário com sucesso")
    void changeUserName_Success() {
        UUID id = UUID.randomUUID();
        String newName = "Teste123";

        userService.changeUserName(id, newName);

        verify(userValidation).userExistsById(id);
        verify(usuarioRepository).changeUserNameTo(newName, id);
    }

    @Test
    @DisplayName("Deve alterar a role do usuário com sucesso")
    void changeUserRole_Success() {
        UUID id = UUID.randomUUID();
        Role role = Role.MEMBER;

        userService.changeUserRole(id, role);

        verify(userValidation).userExistsById(id);
        verify(usuarioRepository).changeUserRoleTo(role, id);
    }

    @Test
    @DisplayName("Deve atualizar o status da entidade com sucesso")
    void updateEntityStatus_Success() {
        UUID id = UUID.randomUUID();
        EntityStatus entityStatus = EntityStatus.INACTIVE;

        userService.updateEntityStatus(entityStatus, id);

        verify(userValidation).userExistsById(id);
        verify(usuarioRepository).changeEntityStatusTo(entityStatus, id);
    }
}