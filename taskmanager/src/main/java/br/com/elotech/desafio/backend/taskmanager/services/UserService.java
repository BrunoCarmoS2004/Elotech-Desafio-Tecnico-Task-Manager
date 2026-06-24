package br.com.elotech.desafio.backend.taskmanager.services;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.UserGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.UserPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.EntityStatus;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.Role;
import br.com.elotech.desafio.backend.taskmanager.domain.models.User;
import br.com.elotech.desafio.backend.taskmanager.domain.repositories.UserRepository;
import br.com.elotech.desafio.backend.taskmanager.exceptions.NotFoundException;
import br.com.elotech.desafio.backend.taskmanager.exceptions.UnauthorizedException;
import br.com.elotech.desafio.backend.taskmanager.mappers.UserMapper;
import br.com.elotech.desafio.backend.taskmanager.security.dtos.gets.UserWithTokenGetDTO;
import br.com.elotech.desafio.backend.taskmanager.security.dtos.gets.UserLoginValidationGetDTO;
import br.com.elotech.desafio.backend.taskmanager.security.services.TokenService;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import br.com.elotech.desafio.backend.taskmanager.validation.UserValidation;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Cacheable(value = "userListCache", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public PagedModel<UserGetDTO> getAll(Pageable pageable) {
        return new PagedModel<>(usuarioRepository.findBy(pageable, UserGetDTO.class));
    }

    @Cacheable(value = "userCache", key = "#id")
    public UserGetDTO getUserById(UUID id) {
        return usuarioRepository.findById(id, UserGetDTO.class).orElseThrow(
                () -> new NotFoundException(messageUtils.getMessage("user.not-found"))
        );
    }

    @Cacheable(value = "userCache", key = "#email")
    public UserGetDTO getUserByEmail(String email) {
        return usuarioRepository.findByEmail(email, UserGetDTO.class).orElseThrow(
                () -> new NotFoundException(messageUtils.getMessage("user.not-found"))
        );
    }
    
    @CacheEvict(value = "userListCache", allEntries = true)
    public UserWithTokenGetDTO postUser(@Valid UserPostDTO userPostDTO) {
        userValidation.userExistsByEmail(userPostDTO.email());
        User user = saveAndReturn(userPostDTO);
        generateTokenResponse(user);
        return userMapper.userToUserWithTokenGetDTO(user);
    }

    @Caching(evict = {
            @CacheEvict(value = "userCache", key = "#id"),
            @CacheEvict(value = "userListCache", allEntries = true)
    })
    public void changeUserName(UUID id, String name) {
        validateUserExists(id);
        usuarioRepository.changeUserNameTo(name, id);
    }

    @Caching(evict = {
            @CacheEvict(value = "userCache", key = "#id"),
            @CacheEvict(value = "userListCache", allEntries = true)
    })
    public void changeUserRole(UUID id, Role role) {
        validateUserExists(id);
        usuarioRepository.changeUserRoleTo(role, id);
    }

    @Caching(evict = {
            @CacheEvict(value = "userCache", key = "#id"),
            @CacheEvict(value = "userListCache", allEntries = true)
    })
    public void updateEntityStatus(EntityStatus entityStatus, UUID id) {
        validateUserExists(id);
        usuarioRepository.changeEntityStatusTo(entityStatus, id);
    }

    protected void validateUserExists(UUID id) {
        userValidation.userExistsById(id);
    }

    protected void validateUsersExists(List<UUID> ids) {
        userValidation.usersExistisByIds(ids);
    }

    protected User getReferenceById(UUID userId) {
        return usuarioRepository.getReferenceById(userId);
    }

    private User saveAndReturn(UserPostDTO userPostDTO) {
        User user = userMapper.userPostDTOToUser(userPostDTO);
        user.setPassword(passwordEncoder.encode(userPostDTO.password()));
        return usuarioRepository.save(user);
    }

    private void generateTokenResponse(User user){
        UserLoginValidationGetDTO userTokenGetDTO = new UserLoginValidationGetDTO(user.getId(), user.getRole(), user.getEmail(), user.getPassword());
        user.setTokenResponse(tokenService.generateTokenResponse(userTokenGetDTO));
    }

}
