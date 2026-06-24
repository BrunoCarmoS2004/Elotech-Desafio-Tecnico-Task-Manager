package br.com.elotech.desafio.backend.taskmanager.services.integrations;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.UserGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.UserPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.EntityStatus;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.Role;
import br.com.elotech.desafio.backend.taskmanager.domain.models.User;
import br.com.elotech.desafio.backend.taskmanager.domain.repositories.UserRepository;
import br.com.elotech.desafio.backend.taskmanager.exceptions.UnauthorizedException;
import br.com.elotech.desafio.backend.taskmanager.security.dtos.gets.UserLoginValidationGetDTO;
import br.com.elotech.desafio.backend.taskmanager.security.dtos.gets.UserWithTokenGetDTO;
import br.com.elotech.desafio.backend.taskmanager.security.services.TokenService;
import br.com.elotech.desafio.backend.taskmanager.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private TokenService tokenService;

    private User usuarioTeste;

    @BeforeEach
    void setUp() {
        usuarioTeste = new User();
        usuarioTeste.setEmail("teste@elotech.com");
        usuarioTeste.setPassword(passwordEncoder.encode("123123123"));
        usuarioTeste.setName("Usuário de Teste MEMEBER");
        usuarioTeste.setRole(Role.MEMBER);
        usuarioTeste.setEntityStatus(EntityStatus.ACTIVE);

        userRepository.save(usuarioTeste);
    }

    @Test
    @DisplayName("Deve validar o login com sucesso buscando do banco de dados")
    void validateLogin_Success() {
        UserLoginValidationGetDTO result = userService.validateLogin("teste@elotech.com", "123123123");

        assertNotNull(result);
        assertEquals("teste@elotech.com", result.email());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar validar login com senha incorreta")
    void validateLogin_Unauthorized() {
        assertThrows(UnauthorizedException.class, () ->
                userService.validateLogin("teste@elotech.com", "123123124")
        );
    }

    @Test
    @DisplayName("Deve buscar usuário por ID existente no banco de dados")
    void getUserById_Success() {
        UserGetDTO result = userService.getUserById(usuarioTeste.getId());

        assertNotNull(result);
        assertEquals("Usuário de Teste MEMEBER", result.name());
        assertEquals("teste@elotech.com", result.email());
    }

    @Test
    @DisplayName("Deve criar um novo usuário e persistir no banco de dados")
    void postUser_Success() {
        when(tokenService.generateTokenResponse(any())).thenReturn(null);

        UserPostDTO novoUsuarioDTO = new UserPostDTO(
                "Novo Usuário MANAGER",
                "novo@elotech.com",
                "123123123",
                Role.MANAGER
        );

        UserWithTokenGetDTO result = userService.postUser(novoUsuarioDTO);

        assertNotNull(result);
        assertEquals("novo@elotech.com", result.user().email());

        UserLoginValidationGetDTO usuarioSalvo = userRepository.findByEmail("novo@elotech.com").orElseThrow();
        assertNotNull(usuarioSalvo.id());

        assertNotEquals("123123123", usuarioSalvo.password());
        assertTrue(passwordEncoder.matches("123123123", usuarioSalvo.password()));
    }

    @Test
    @DisplayName("Deve alterar o nome do usuário no banco de dados")
    void changeUserName_Success() {
        String novoNome = "Nome Alterado";

        userService.changeUserName(usuarioTeste.getId(), novoNome);

        User usuarioAtualizado = userRepository.findById(usuarioTeste.getId()).orElseThrow();
        assertEquals(novoNome, usuarioAtualizado.getName());
    }
}