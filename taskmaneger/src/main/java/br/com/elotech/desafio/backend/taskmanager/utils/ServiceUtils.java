package br.com.elotech.desafio.backend.taskmanager.utils;

import br.com.elotech.desafio.backend.taskmanager.responses.ResponsePayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class ServiceUtils {

    public static <T> ResponseEntity<ResponsePayload<T>> createResponse(HttpStatus httpStatus, UUID id, T dto, String message) {
        return ResponseEntity.status(httpStatus).body(new ResponsePayload<>(id, message, dto));
    }
}
