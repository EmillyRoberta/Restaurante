package dtos;

import br.com.fiap.restaurante.restaurante.entities.UserType;

public record UserResponse(
        Long id,
        String name,
        String email,
        String login,
        AddressResponse address,
        UserType userType
) {

}
