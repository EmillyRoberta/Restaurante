package dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
        @NotNull
        @NotBlank(message = "name is required")
        String name,

        @NotNull
        @NotBlank(message = "Email is required")
        String email,

        @NotNull
        @NotBlank(message = "login is required")
        String login,

        @NotNull
        AddressRequest address
) {

}
