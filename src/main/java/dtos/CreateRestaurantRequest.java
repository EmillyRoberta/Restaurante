package dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRestaurantRequest(
        @NotNull
        @NotBlank(message="name is required")
        String name,

        String description,

        @NotNull
        Long ownerId
) {
}
