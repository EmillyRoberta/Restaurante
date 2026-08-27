package dtos;

import jakarta.validation.constraints.NotBlank;

public record AddressResponse(
        @NotBlank(message = "'street' is required")
        String street,

        @NotBlank(message = "'number' is required")
        String number,

        @NotBlank(message = "'city' is required")
        String city,

        @NotBlank(message = "'state' is required")
        String state,

        @NotBlank(message = "'state' is required")
        String zipCode
) {
}
