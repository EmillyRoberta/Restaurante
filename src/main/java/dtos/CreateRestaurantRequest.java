package dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateRestaurantRequest(
        @NotBlank(message = "'name' is required")
        String name,

        @Nullable
        String description,

        @Positive(message = "'ownerId' must be a positive ID number")
        Long ownerId
) {
}
