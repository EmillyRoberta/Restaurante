package br.com.fiap.restaurante.restaurante.controllers;


import br.com.fiap.restaurante.restaurante.controllers.types.HttpStatusCode;
import br.com.fiap.restaurante.restaurante.services.UserService;
import dtos.LoginRequest;
import dtos.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    @Operation(description = "Authenticates.",
            summary = "Authenticates.")
    @ApiResponses(value = {
            @ApiResponse(description = "Success.", responseCode = HttpStatusCode.OK),
            @ApiResponse(description = "Login or password does not match.", responseCode = HttpStatusCode.UNAUTHORIZED,
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    }
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequest request) throws LoginException {

        UserResponse user = userService.validateLogin(request);
        return ResponseEntity.ok("User " + user.login() + " is logged in.");
    }
}
