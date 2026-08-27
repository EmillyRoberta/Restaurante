package dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
        @NotBlank(message = "'name' is required")
        String name,

        @NotBlank(message = "'email' is required")
        String email,

        @NotBlank(message = "'login' is required")
        String login,

        @NotNull
        AddressRequest address
) {

}
