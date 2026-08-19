package dtos;

import br.com.fiap.restaurante.restaurante.entities.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotNull
        @NotBlank(message="Name is required")
        String name,

        @NotNull
        @NotBlank(message="Email is required")
        String email,

        @NotNull
        @NotBlank(message="Login is required")
        String login,

        @NotNull
        @NotBlank(message="Password is required")
        String password,

        @NotNull
        AddressRequest address,

        @NotNull
        UserType userType
) {
}
