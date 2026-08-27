package dtos;

import br.com.fiap.restaurante.restaurante.entities.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotBlank(message = "'name' is required")
        String name,

        @NotBlank(message = "'email' is required")
        String email,

        @NotBlank(message = "'login' is required")
        String login,

        @NotBlank(message = "'password' is required")
        String password,

        @NotNull(message = "'address' is required")
        AddressRequest address,

        @NotNull(message = "'userType' is required")
        UserType userType
) {

    @Override
    public String toString() {
        return "CreateUserRequest{" +
               "name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", login='" + login + '\'' +
               ", password='[PROTECTED]'" +
               ", address=" + address +
               ", userType=" + userType +
               '}';
    }
}
