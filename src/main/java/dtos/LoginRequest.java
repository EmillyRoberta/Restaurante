package dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "'login' is required")
        String login,

        @NotBlank(message = "'password' is required")
        String password
) {
}
