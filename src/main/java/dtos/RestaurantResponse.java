package dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RestaurantResponse(
        @NotNull(message = "'id' is required")
        Long id,

        @NotBlank(message = "'name' is required")
        String name,

        @Nullable
        String description,

        @NotNull(message = "'ownerId' is required")
        Long ownerId
) {
}
