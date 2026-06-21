package br.com.elotech.desafio.backend.taskmaneger.services;

import br.com.elotech.desafio.backend.taskmaneger.domain.dtos.gets.UserGetDTO;
import br.com.elotech.desafio.backend.taskmaneger.domain.dtos.posts.UserPostDTO;
import br.com.elotech.desafio.backend.taskmaneger.domain.enums.EntityStatus;
import br.com.elotech.desafio.backend.taskmaneger.domain.enums.Role;
import br.com.elotech.desafio.backend.taskmaneger.domain.models.User;
import br.com.elotech.desafio.backend.taskmaneger.domain.repositories.UserRepository;
import br.com.elotech.desafio.backend.taskmaneger.exceptions.NotFoundException;
import br.com.elotech.desafio.backend.taskmaneger.exceptions.UnauthorizedException;
import br.com.elotech.desafio.backend.taskmaneger.mappers.UserMapper;
import br.com.elotech.desafio.backend.taskmaneger.security.dtos.gets.UserLoginValidationGetDTO;
import br.com.elotech.desafio.backend.taskmaneger.security.services.TokenService;
import br.com.elotech.desafio.backend.taskmaneger.utils.MessageUtils;
import br.com.elotech.desafio.backend.taskmaneger.validation.UserValidation;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserMapper userMapper;

    private final UserRepository usuarioRepository;

    private final UserValidation userValidation;

    private final MessageUtils messageUtils;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    public UserService(UserMapper userMapper, MessageUtils messageUtils, UserRepository usuarioRepository, UserValidation userValidation, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userMapper = userMapper;
        this.messageUtils = messageUtils;
        this.usuarioRepository = usuarioRepository;
        this.userValidation = userValidation;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public UserLoginValidationGetDTO validateLogin(String email, String password) {
        UserLoginValidationGetDTO userLoginValidationGetDTO = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new UnauthorizedException(messageUtils.getMessage("user.invalid-credentials"))
        );
        userValidation.passwordEncoderMatches(password, userLoginValidationGetDTO.password());
        return userLoginValidationGetDTO;
    }

    public UserLoginValidationGetDTO validateRefresh(UUID id) {
        return usuarioRepository.findById(id, UserLoginValidationGetDTO.class).orElseThrow(
                () -> new UnauthorizedException(messageUtils.getMessage("user.invalid-credentials"))
        );
    }

    @Cacheable(value = "usersPage")
    public PagedModel<UserGetDTO> getAll(Pageable pageable) {
        return new PagedModel<>(usuarioRepository.findBy(pageable, UserGetDTO.class));
    }

    @Cacheable(value = "userById", key = "#id")
    public UserGetDTO getUserById(UUID id) {
        return usuarioRepository.findById(id, UserGetDTO.class).orElseThrow(
                () -> new NotFoundException(messageUtils.getMessage("user.not-found"))
        );
    }

    @CacheEvict(value = "usersPage", allEntries = true)
    public UserGetDTO postUser(@Valid UserPostDTO userPostDTO) {
        userValidation.userExistsByEmail(userPostDTO.email());
        User user = userMapper.userPostDTOToUser(userPostDTO);
        user.setPassword(passwordEncoder.encode(userPostDTO.password()));
        usuarioRepository.save(user);
        UserLoginValidationGetDTO userTokenGetDTO = new UserLoginValidationGetDTO(user.getId(), user.getRole(), user.getPassword());
        user.setTokenResponse(tokenService.generateTokenResponse(userTokenGetDTO));
        return userMapper.userToUserGetDTO(user);
    }

    @Caching(evict = {
            @CacheEvict(value = "userById", key = "#id"),
            @CacheEvict(value = "userRefresh", key = "#id"),
            @CacheEvict(value = "usersPage", allEntries = true)
    })
    public void changeUserName(UUID id, String name) {
        validateUserExists(id);
        usuarioRepository.changeUserNameTo(name, id);
    }

    @Caching(evict = {
            @CacheEvict(value = "userById", key = "#id"),
            @CacheEvict(value = "userRefresh", key = "#id"),
            @CacheEvict(value = "usersPage", allEntries = true)
    })
    public void changeUserRole(UUID id, Role role) {
        validateUserExists(id);
        usuarioRepository.changeUserRoleTo(role, id);
    }

    @Caching(evict = {
            @CacheEvict(value = "userById", key = "#id"),
            @CacheEvict(value = "userRefresh", key = "#id"),
            @CacheEvict(value = "usersPage", allEntries = true)
    })
    public void updateEntityStatus(EntityStatus entityStatus, UUID id) {
        validateUserExists(id);
        usuarioRepository.changeEntityStatusTo(entityStatus, id);
    }

    private void validateUserExists(UUID id) {
        userValidation.userExistsById(id);
    }
}
