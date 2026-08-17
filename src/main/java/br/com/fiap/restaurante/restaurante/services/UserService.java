package br.com.fiap.restaurante.restaurante.services;

import br.com.fiap.restaurante.restaurante.entities.Address;
import br.com.fiap.restaurante.restaurante.entities.User;
import br.com.fiap.restaurante.restaurante.repositories.UserRepository;
import dtos.AddressResponse;
import dtos.CreateUserRequest;
import dtos.UpdateUserRequest;
import dtos.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository userRepository;

    public UserResponse createUser(CreateUserRequest request) {
        if(userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already registered");
        }

        if(userRepository.existsByLogin(request.login())) {
            throw new RuntimeException("Login already registered");
        }

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

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getLogin(),
                new AddressResponse(
                        savedUser.getAddress().getStreet(),
                        savedUser.getAddress().getNumber(),
                        savedUser.getAddress().getCity(),
                        savedUser.getAddress().getState(),
                        savedUser.getAddress().getZipCode()
                        ),
                savedUser.getUserType()
        );
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

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getLogin(),
                new AddressResponse(
                        savedUser.getAddress().getStreet(),
                        savedUser.getAddress().getNumber(),
                        savedUser.getAddress().getCity(),
                        savedUser.getAddress().getState(),
                        savedUser.getAddress().getZipCode()
                ),
                savedUser.getUserType()
        );
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }

    private UserResponse toResponse(User user) {
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

    public UserResponse findUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return toResponse(user);
    }

    public Page<UserResponse> findAllUsers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return userRepository.findAll(pageable)
                .map(this::toResponse);
    }
}
