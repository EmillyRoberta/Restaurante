package dtos;

import br.com.fiap.restaurante.restaurante.entities.User;
import br.com.fiap.restaurante.restaurante.entities.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserResponse(
        @NotNull(message = "'id' is required")
        Long id,

        @NotBlank(message = "'name' is required")
        String name,

        @NotBlank(message = "'email' is required")
        String email,

        @NotBlank(message = "'login' is required")
        String login,

        @NotNull(message = "'address' is required")
        AddressResponse address,

        @NotNull(message = "'userType' is required")
        UserType userType
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                new AddressResponse(
                        user.getAddress().getStreet(),
                        user.getAddress().getNumber(),
                        user.getAddress().getCity(),
                        user.getAddress().getState(),
                        user.getAddress().getZipCode()
                ),
                user.getUserType()
        );
    }

}
