package br.com.fiap.restaurante.restaurante.controllers.handlers;

import br.com.fiap.restaurante.restaurante.services.exceptions.BusinessException;
import br.com.fiap.restaurante.restaurante.services.exceptions.NonUniqueFieldException;
import br.com.fiap.restaurante.restaurante.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import javax.security.auth.login.LoginException;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(NonUniqueFieldException.class)
    public ProblemDetail handlerNonUniqueFieldException(
            NonUniqueFieldException nonUniqueFieldException) {
        LOGGER.info(nonUniqueFieldException.getMessage());
        return buildProblemDetail(HttpStatus.BAD_REQUEST, nonUniqueFieldException);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handlerResourceNotFoundException(
            ResourceNotFoundException resourceNotFoundException) {
        LOGGER.info(resourceNotFoundException.getMessage());

        return buildProblemDetail(HttpStatus.NOT_FOUND, resourceNotFoundException);
    }

    @ExceptionHandler(LoginException.class)
    public ProblemDetail handlerResourceNotFoundException(
            LoginException loginException) {
        LOGGER.warn(loginException.getMessage());

        return buildProblemDetail(HttpStatus.UNAUTHORIZED, loginException);
    }

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handlerBusinessException(
            BusinessException businessException) {
        LOGGER.error(businessException.getMessage());

        return buildProblemDetail(HttpStatus.UNPROCESSABLE_CONTENT, businessException);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        LOGGER.info(ex.getMessage());

        ProblemDetail body = ex.getBody();

        body.setTitle("Validation Failed");
        body.setDetail("One or more fields failed validation checks.");
        body.setStatus(HttpStatus.BAD_REQUEST);

        // Extract invalid fields cleanly into a structured map
        Map<String, String> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                                                 .collect(Collectors.toMap(
                                                         FieldError::getField,
                                                         fieldError -> fieldError.getDefaultMessage() != null ?
                                                                       fieldError.getDefaultMessage() : "Invalid value",
                                                         (existing, replacement) -> existing // keeps first error if duplicate keys exist
                                                 ));

        // Add extensions to RFC 7807 structure
        body.setProperty("invalid_fields", validationErrors);

        return body;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handlerMethodValidationException(
            HandlerMethodValidationException ex) {
        LOGGER.info(ex.getMessage());

        return buildProblemDetail(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handlerHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        LOGGER.info(ex.getMessage());

        return buildProblemDetail(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handlerIllegalArgumentException(
            IllegalArgumentException ex) {
        LOGGER.info(ex.getMessage());

        return buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private ProblemDetail buildProblemDetail(HttpStatus httpStatus, RuntimeException ex) {
        return buildProblemDetail(httpStatus, ex.getMessage());
    }

    private ProblemDetail buildProblemDetail(HttpStatus httpStatus, Exception ex) {
        return buildProblemDetail(httpStatus, ex.getMessage());
    }

    private ProblemDetail buildProblemDetail(HttpStatus httpStatus, String message) {
        var problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, message);
        problemDetail.setProperty("TimeStamp", Instant.now());
        return problemDetail;
    }
}
