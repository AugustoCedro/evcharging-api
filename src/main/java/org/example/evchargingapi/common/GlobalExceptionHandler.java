package org.example.evchargingapi.common;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.evchargingapi.exceptions.EntityAlreadyExistsException;
import org.example.evchargingapi.exceptions.EntityIsNotAvailableException;
import org.example.evchargingapi.model.dtos.ErrorDTO;
import org.example.evchargingapi.model.dtos.ErrorFieldDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.authorization.method.HandleAuthorizationDenied;
import org.springframework.security.authorization.method.MethodAuthorizationDeniedHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorDTO> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex){
        log.error("Entity Already Exists ERROR: {}",ex.getMessage());
        var error = new ErrorDTO(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDTO> handleEntityNotFoundException(EntityNotFoundException ex){
        log.error("Entity Not Found Exception: {}",ex.getMessage());
        var error = new ErrorDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EntityIsNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleEntityIsNotAvailableException(EntityIsNotAvailableException ex){
        log.error("Entity is Not Available: {}",ex.getMessage());
        var error = new ErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        log.error("Validation Error: {}",ex.getMessage());
        List<FieldError> errorList = ex.getFieldErrors();
        List<ErrorFieldDTO> errorFieldDTOS = errorList.stream().map(error -> new ErrorFieldDTO(error.getField(),error.getDefaultMessage())).toList();
        var error = new ErrorDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de Validação",
                errorFieldDTOS
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDTO> handleRuntimeException(RuntimeException ex){
        log.error("Internal Server Error: {}",ex.getStackTrace());
        var error = new ErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno do servidor",
                List.of()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorDTO> handleAuthorizationDeniedException(AuthorizationDeniedException ex){
        log.error("Authorization Denied: {}",ex.getMessage());
        var error = new ErrorDTO(
                HttpStatus.FORBIDDEN.value(),
                "Entidade não possui permissão",
                List.of()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

}
