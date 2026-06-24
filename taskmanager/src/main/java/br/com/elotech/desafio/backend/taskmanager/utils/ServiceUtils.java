package br.com.elotech.desafio.backend.taskmanager.utils;

import br.com.elotech.desafio.backend.taskmanager.domain.enums.Role;
import br.com.elotech.desafio.backend.taskmanager.responses.ResponsePayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public class ServiceUtils {

    public static <T> ResponseEntity<ResponsePayload<T>> createResponse(HttpStatus httpStatus, UUID id, T dto, String message) {
        return ResponseEntity.status(httpStatus).body(new ResponsePayload<>(id, message, dto));
    }

    public static Role getRoleFromToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            return Role.valueOf(jwt.getClaimAsString("roles"));
        }
        return null;
    }

    public static UUID getUserIdFromToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            return UUID.fromString(jwt.getSubject());
        }
        return null;
    }
}
