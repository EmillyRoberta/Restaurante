package br.com.fiap.restaurante.restaurante.services;

import br.com.fiap.restaurante.restaurante.entities.Address;
import br.com.fiap.restaurante.restaurante.entities.User;
import br.com.fiap.restaurante.restaurante.repositories.RestaurantRepository;
import br.com.fiap.restaurante.restaurante.repositories.UserRepository;
import br.com.fiap.restaurante.restaurante.services.exceptions.BusinessException;
import br.com.fiap.restaurante.restaurante.services.exceptions.NonUniqueFieldException;
import br.com.fiap.restaurante.restaurante.services.exceptions.ResourceNotFoundException;
import dtos.CreateUserRequest;
import dtos.UpdateUserRequest;
import dtos.UserResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public UserResponse createUser(CreateUserRequest request) {
        validateUniqueFields(request.email(), request.login(), null);

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
                                  .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validateUniqueFields(request.email(), request.login(), id);

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
                                  .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (restaurantRepository.existsByOwnerId(id)) {
            throw new BusinessException(
                    "User cannot be deleted because owns one or more restaurants."
            );
        }

        userRepository.delete(user);
    }

    public UserResponse findUserById(Long id) {

        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new ResourceNotFoundException("User not found."));

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

    private void validateUniqueFields(String email, String login, Long userId) throws NonUniqueFieldException {
        validateEmailPattern(email);

        //TODO: otimizar essas validações de email e login em uma melhoria futura.
        var userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent() && !userByEmail.get().getId().equals(userId)) {
            throw new NonUniqueFieldException("Error: Email already registered");
        }

        var userByLogin = userRepository.findByLogin(login);
        if (userByLogin.isPresent() && !userByLogin.get().getId().equals(userId)) {
            throw new NonUniqueFieldException("Error: Login already registered");
        }
    }

    public void validateEmailPattern(String email) throws BusinessException {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new BusinessException("Error: Invalid Email format.");
        }
    }

    public User findUserByLogin(String login) throws LoginException {
        return userRepository.findByLogin(login)
                             .orElseThrow(() -> new LoginException("Invalid login"));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
