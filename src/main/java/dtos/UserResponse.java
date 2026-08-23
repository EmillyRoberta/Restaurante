package dtos;

import br.com.fiap.restaurante.restaurante.entities.User;
import br.com.fiap.restaurante.restaurante.entities.UserType;

public record UserResponse(
        Long id,
        String name,
        String email,
        String login,
        AddressResponse address,
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
