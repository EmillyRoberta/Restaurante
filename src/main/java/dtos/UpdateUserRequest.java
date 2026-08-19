package dtos;

import br.com.fiap.restaurante.restaurante.entities.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateUserRequest(
        @NotNull
        @NotBlank(message="name is required")
        String name,

        @NotNull
        @NotBlank(message="Email is required")
        String email,

        @NotNull
        @NotBlank(message="login is required")
        String login,

        @NotNull
        AddressRequest address
) {

}
