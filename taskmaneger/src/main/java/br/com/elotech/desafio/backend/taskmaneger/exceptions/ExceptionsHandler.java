package br.com.elotech.desafio.backend.taskmaneger.exceptions;

import br.com.elotech.desafio.backend.taskmaneger.utils.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ExceptionsHandler {

    private final MessageUtils messageUtils;

    public ExceptionsHandler(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    @ExceptionHandler(value = Exception.class)
    public ProblemDetail handleInternalServerException(Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        problemDetail.setTitle(messageUtils.getMessage("error.INTERNAL_SERVER_ERROR"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
    
    @ExceptionHandler(value = UnauthorizedException.class)
    public ProblemDetail handleUnauthorizedException(UnauthorizedException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        problemDetail.setTitle(messageUtils.getMessage("error.UNAUTHORIZED"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ProblemDetail handleUnauthorizedException(NotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle(messageUtils.getMessage("error.NOT_FOUND"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
