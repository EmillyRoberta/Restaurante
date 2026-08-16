package dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequest(
        @NotNull
        @NotBlank(message="street is required")
        String street,

        @NotNull
        @NotBlank(message="number is required")
        String number,

        @NotNull
        @NotBlank(message="city is required")
        String city,

        @NotNull
        @NotBlank(message="state is required")
        String state,

        @NotNull
        @NotBlank(message="state is required")
        String zipCode
) {
}
