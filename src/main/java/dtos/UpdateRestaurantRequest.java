package dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateRestaurantRequest(

        @NotBlank(message = "'name' is required")
        String name,

        @NotBlank(message = "'description' is required")
        String description,

        @NotNull(message = "'ownerId' id is required")
        @Positive(message = "'ownerId' must be a positive ")
        Long ownerId
) {
}
