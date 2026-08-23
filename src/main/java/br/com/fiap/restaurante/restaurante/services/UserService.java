package br.com.fiap.restaurante.restaurante.services;

import br.com.fiap.restaurante.restaurante.entities.Address;
import br.com.fiap.restaurante.restaurante.entities.User;
import br.com.fiap.restaurante.restaurante.repositories.RestaurantRepository;
import br.com.fiap.restaurante.restaurante.repositories.UserRepository;
import dtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository userRepository;
    public final RestaurantRepository restaurantRepository;

    public UserResponse createUser(CreateUserRequest request) {
        validateUniqueFields(request.email(), request.login());

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .login(request.login())
                .password(request.password())
                .address(
                        Address.builder()
                                .street(request.address().street())
                                .number(request.address().number())
                                .city(request.address().city())
                                .state(request.address().state())
                                .zipCode(request.address().zipCode())
                                .build()
                )
                .userType(request.userType())
                .lastUpdate(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        return UserResponse.fromEntity(savedUser);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.name());
        user.setEmail(request.email());
        user.setLogin(request.login());

        user.setAddress(
                Address.builder()
                        .street(request.address().street())
                        .number(request.address().number())
                        .city(request.address().city())
                        .state(request.address().state())
                        .zipCode(request.address().zipCode())
                        .build()
        );

        user.setLastUpdate(LocalDateTime.now());

        User savedUser = userRepository.save(user);

       return UserResponse.fromEntity(savedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (restaurantRepository.existsByOwnerId(id)) {
            throw new RuntimeException(
                    "User cannot be deleted because owns one or more restaurants"
            );
        }

        userRepository.delete(user);
    }

    public UserResponse findUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.fromEntity(user);
    }

    public Page<UserResponse> findAllUsers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return userRepository.findAll(pageable)
                .map(UserResponse::fromEntity);
    }

    public List<UserResponse> findByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    private void validateUniqueFields(String email, String login) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        if (userRepository.existsByLogin(login)) {
            throw new RuntimeException("Login already registered");
        }
    }

    public UserResponse validateLogin(LoginRequest request) {
        User user = userRepository.findByLogin(request.login())
                .orElseThrow(() -> new RuntimeException("Invalid login or password"));

        if (!user.getPassword().equals(request.password())) {
            throw new RuntimeException("Invalid login or password");
        }

        return UserResponse.fromEntity(user);
    }
}
