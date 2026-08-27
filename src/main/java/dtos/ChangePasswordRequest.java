package dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "'login' is required.")
        String login,
        @NotBlank(message = "'oldPassword' is required.")
        @Size(min = 6, max = 15, message = "'oldPassword' must be between {min} and {max} characters long")
        String oldPassword,
        @NotBlank(message = "'newPassword' is required.")
        @Size(min = 6, max = 15, message = "'newPassword' must be between {min} and {max} characters long")
        String newPassword
) {
}
