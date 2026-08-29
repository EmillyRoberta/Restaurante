package br.com.fiap.restaurante.restaurante.controllers.handlers;

import br.com.fiap.restaurante.restaurante.entities.UserType;
import br.com.fiap.restaurante.restaurante.services.exceptions.BusinessException;
import br.com.fiap.restaurante.restaurante.services.exceptions.NonUniqueFieldException;
import br.com.fiap.restaurante.restaurante.services.exceptions.ResourceNotFoundException;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tools.jackson.databind.exc.InvalidFormatException;

import javax.security.auth.login.LoginException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        LOGGER.info(ex.getMessage());

        var problemDetail = ex.getBody();

        problemDetail.setTitle("Validation Failed");
        problemDetail.setDetail("One or more fields failed validation checks.");
        problemDetail.setStatus(HttpStatus.BAD_REQUEST);

        // Extract invalid fields into a map
        Map<String, String> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                                                 .collect(Collectors.toMap(
                                                         FieldError::getField,
                                                         fieldError -> fieldError.getDefaultMessage() != null ?
                                                                       fieldError.getDefaultMessage() : "Invalid value",
                                                         (existing, replacement) -> existing // keeps first error if duplicate keys exist
                                                 ));

        // Add extensions to RFC 7807 structure
        problemDetail.setProperty("invalid_fields", validationErrors);

        return new ResponseEntity<>(problemDetail, status);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        LOGGER.info(ex.getMessage());
        var problemDetail = buildProblemDetail(HttpStatus.BAD_REQUEST, ex);

        return new ResponseEntity<>(problemDetail, status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handlerIllegalArgumentException(
            IllegalArgumentException ex) {
        LOGGER.info(ex.getMessage());

        return buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }


    //  Exceptions that are managed by 'ResponseEntityExceptionHandler' must be overriden, so they can be used with Problem Detail.
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        LOGGER.info("Validation error list : " + ex.getMessage());
        ProblemDetail body = buildProblemDetail(HttpStatus.BAD_REQUEST, ex);
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException invalidFormat) {
            if (invalidFormat.getTargetType().isEnum()) {
                String fieldName = invalidFormat.getPath().get(0).getPropertyName();
                String rejectedValue = invalidFormat.getValue().toString();
                body.setDetail("Invalid value '" + rejectedValue + "' for field '" + fieldName +
                               "'. Expected values are: " + Arrays.toString(UserType.values()));
            }
        }

        return new ResponseEntity<>(body, status);
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
