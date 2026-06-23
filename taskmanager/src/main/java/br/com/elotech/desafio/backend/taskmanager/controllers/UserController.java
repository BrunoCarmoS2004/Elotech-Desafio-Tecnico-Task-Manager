package br.com.elotech.desafio.backend.taskmanager.controllers;

import br.com.elotech.desafio.backend.taskmanager.domain.dtos.gets.UserGetDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.dtos.posts.UserPostDTO;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.EntityStatus;
import br.com.elotech.desafio.backend.taskmanager.domain.enums.Role;
import br.com.elotech.desafio.backend.taskmanager.responses.ResponsePayload;
import br.com.elotech.desafio.backend.taskmanager.security.dtos.gets.UserWithTokenGetDTO;
import br.com.elotech.desafio.backend.taskmanager.services.UserService;
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
@RequestMapping("/user")
public class UserController {

    private final UserService usuarioService;

    private final MessageUtils messageUtils;

    public UserController(UserService usuarioService, MessageUtils messageUtils) {
        this.usuarioService = usuarioService;
        this.messageUtils = messageUtils;
    }

    @GetMapping
    public ResponseEntity<PagedModel<UserGetDTO>> getAll(@PageableDefault(sort = {"name"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload<UserGetDTO>> getUserById(@PathVariable UUID id) {
        UserGetDTO userGetDTO = usuarioService.getUserById(id);
        return createResponse(
                HttpStatus.OK,
                userGetDTO.id(),
                userGetDTO,
                messageUtils.getMessage("user.found")
        );
    }

    @PostMapping
    public ResponseEntity<ResponsePayload<UserWithTokenGetDTO>> postUser(@Valid @RequestBody UserPostDTO userPostDTO) {
        UserWithTokenGetDTO userWithTokenGetDTO = usuarioService.postUser(userPostDTO);
        return createResponse(
                HttpStatus.CREATED,
                userWithTokenGetDTO.user().id(),
                userWithTokenGetDTO,
                messageUtils.getMessage("user.created")
        );
    }

    @PatchMapping("/change/{id}/name")
    public ResponseEntity<ResponsePayload<String>> changeUserName(@PathVariable UUID id, @RequestParam String name) {
        usuarioService.changeUserName(id, name);
        return createResponse(
                HttpStatus.OK,
                id,
                name,
                messageUtils.getMessage("user.name.updated")
        );
    }

    @PatchMapping("/change/{id}/role")
    public ResponseEntity<ResponsePayload<String>> changeUserRole(@PathVariable UUID id, @RequestParam Role role) {
        usuarioService.changeUserRole(id, role);
        return createResponse(
                HttpStatus.OK,
                id,
                role.name(),
                messageUtils.getMessage("user.role.updated")
        );
    }

    @PatchMapping("/change/{id}/entitystatus")
    public ResponseEntity<ResponsePayload<String>> changeEntityStatus(@PathVariable UUID id, @RequestParam EntityStatus entityStatus) {
        usuarioService.updateEntityStatus(entityStatus, id);
        return createResponse(
                HttpStatus.OK,
                id,
                entityStatus.name(),
                messageUtils.getMessage("entity-status.updated", entityStatus.name())
        );
    }
}