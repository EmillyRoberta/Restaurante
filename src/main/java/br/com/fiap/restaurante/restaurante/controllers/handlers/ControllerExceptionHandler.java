package br.com.fiap.restaurante.restaurante.controllers.handlers;

import br.com.fiap.restaurante.restaurante.services.exceptions.BusinessException;
import br.com.fiap.restaurante.restaurante.services.exceptions.NonUniqueFieldException;
import br.com.fiap.restaurante.restaurante.services.exceptions.ResourceNotFoundException;
import dtos.exceptions.BaseErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.LoginException;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(NonUniqueFieldException.class)
    public ResponseEntity<BaseErrorDTO> handlerNonUniqueFieldException(
            NonUniqueFieldException nonUniqueFieldException) {
        var status = HttpStatus.BAD_REQUEST;
        LOGGER.error(nonUniqueFieldException.getMessage());

        return ResponseEntity.status(status.value())
                             .body(new BaseErrorDTO(nonUniqueFieldException.getMessage(), status.value()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseErrorDTO> handlerResourceNotFoundException(
            ResourceNotFoundException resourceNotFoundException) {
        var status = HttpStatus.NOT_FOUND;
        LOGGER.error(resourceNotFoundException.getMessage());

        return ResponseEntity.status(status.value())
                             .body(new BaseErrorDTO(resourceNotFoundException.getMessage(), status.value()));
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<BaseErrorDTO> handlerResourceNotFoundException(
            LoginException loginException) {
        var status = HttpStatus.UNAUTHORIZED;
        LOGGER.error(loginException.getMessage());

        return ResponseEntity.status(status.value())
                             .body(new BaseErrorDTO(loginException.getMessage(), status.value()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseErrorDTO> handlerBusinessException(
            BusinessException businessException) {
        var status = HttpStatus.UNPROCESSABLE_CONTENT;
        LOGGER.error(businessException.getMessage());

        return ResponseEntity.status(status.value())
                             .body(new BaseErrorDTO(businessException.getMessage(), status.value()));
    }

}
