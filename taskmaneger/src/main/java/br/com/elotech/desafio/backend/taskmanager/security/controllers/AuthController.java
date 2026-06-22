package br.com.elotech.desafio.backend.taskmanager.security.controllers;

import br.com.elotech.desafio.backend.taskmanager.security.dtos.posts.UserLoginPostDTO;
import br.com.elotech.desafio.backend.taskmanager.security.responses.TokenResponse;
import br.com.elotech.desafio.backend.taskmanager.security.dtos.posts.RefreshTokenPostDTO;
import br.com.elotech.desafio.backend.taskmanager.security.dtos.gets.UserLoginValidationGetDTO;
import br.com.elotech.desafio.backend.taskmanager.security.services.TokenService;
import br.com.elotech.desafio.backend.taskmanager.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TokenService tokenService;

    private final UserService userService;

    public AuthController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginPostDTO userLoginPostDTO) {
        UserLoginValidationGetDTO userIdRole = userService.validateLogin(userLoginPostDTO.email(), userLoginPostDTO.password());
        TokenResponse tokenResponse = tokenService.generateTokenResponse(userIdRole);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenPostDTO refreshTokenPostDTO) {
        UserLoginValidationGetDTO userLoginValidationGetDTO = userService.validateRefresh(
                tokenService.validateRefreshTokenEGetUserId(refreshTokenPostDTO.refreshToken())
        );
        TokenResponse tokenResponse = tokenService.generateTokenResponse(userLoginValidationGetDTO);
        return ResponseEntity.ok(tokenResponse);
    }
}