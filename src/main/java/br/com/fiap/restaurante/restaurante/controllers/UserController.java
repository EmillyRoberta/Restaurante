package br.com.fiap.restaurante.restaurante.controllers;

import br.com.fiap.restaurante.restaurante.controllers.types.HttpStatusCode;
import br.com.fiap.restaurante.restaurante.services.AuthService;
import br.com.fiap.restaurante.restaurante.services.UserService;
import dtos.ChangePasswordRequest;
import dtos.CreateUserRequest;
import dtos.UpdateUserRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final AuthService authService;

    @Operation(description = "Creates new user. E-mail and login must be unique.",
            summary = "Creates new user.",
            responses = {
                    @ApiResponse(description = "User created successfully.", responseCode = HttpStatusCode.CREATED),
                    @ApiResponse(description = "Invalid request.", responseCode = HttpStatusCode.BAD_REQUEST,
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PostMapping("/save")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        LOGGER.info("/save - " + request.toString());
        String encodedPassword = authService.encodePassword(request.password());
        CreateUserRequest requestWithEncodedPassword = new CreateUserRequest(request, encodedPassword);

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(requestWithEncodedPassword));
    }

    @Operation(description = "Updates user data. E-mail and login must be unique.",
            summary = "Updates user data.",
            responses = {
                    @ApiResponse(description = "User updated successfully.", responseCode = HttpStatusCode.OK),
                    @ApiResponse(description = "User not found.", responseCode = HttpStatusCode.NOT_FOUND,
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(description = "Invalid request.", responseCode = HttpStatusCode.BAD_REQUEST,
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        LOGGER.info("Update User - " + request.toString());

        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @Operation(description = "Deletes user.",
            summary = "Deletes user.",
            responses = {
                    @ApiResponse(description = "User deleted successfully.", responseCode = HttpStatusCode.OK),
                    @ApiResponse(description = "User not found.", responseCode = HttpStatusCode.NOT_FOUND,
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(description = "User cannot be deleted because owns one or more restaurants.",
                            responseCode = HttpStatusCode.UNPROCESSABLE_CONTENT,
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUser(
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Lists all users.",
            summary = "Lists all users.",
            responses = {
                    @ApiResponse(description = "Success.", responseCode = HttpStatusCode.OK)
            }
    )
    @GetMapping
    public ResponseEntity<Page<UserResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                userService.findAllUsers(page, size)
        );
    }

    @Operation(description = "Lists user by ID.",
            summary = "Lists user by ID.")
    @ApiResponses(value = {
            @ApiResponse(description = "Success.", responseCode = HttpStatusCode.OK),
            @ApiResponse(description = "User not found.", responseCode = HttpStatusCode.NOT_FOUND)
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @Operation(description = "Search user by name.",
            summary = "Search user by name.",
            responses = {
                    @ApiResponse(description = "Success.", responseCode = HttpStatusCode.OK),
                    @ApiResponse(description = "User not found.", responseCode = HttpStatusCode.NOT_FOUND)
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchByName(
            @RequestParam String name) {
        return ResponseEntity.ok(userService.findByName(name));
    }



}
