package br.com.fiap.restaurante.restaurante.services;

import br.com.fiap.restaurante.restaurante.entities.User;
import br.com.fiap.restaurante.restaurante.services.exceptions.BusinessException;
import dtos.ChangePasswordRequest;
import dtos.LoginRequest;
import dtos.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Validates if both passwords matches
     *
     * @param rawPassword
     * @param storedHashedPassword
     * @return
     */
    public void authenticatePassword(String rawPassword, String storedHashedPassword) throws LoginException {

        if (!passwordEncoder.matches(rawPassword, storedHashedPassword)) {
            throw new LoginException("Invalid password");
        }
    }

    /**
     * Validate user credentials
     *
     * @param request
     * @return
     * @throws LoginException
     */
    public UserResponse validateLogin(LoginRequest request) throws LoginException {
        User user = userService.findUserByLogin(request.login());

        authenticatePassword(request.password(), user.getPassword());

        return UserResponse.fromEntity(user);
    }

    public UserResponse changePassword(ChangePasswordRequest request) throws BusinessException, LoginException {
        User user = userService.findUserByLogin(request.login());
        String hashNewPassword = passwordEncoder.encode(request.newPassword());

        LoginRequest loginRequest = new LoginRequest(request.login(), request.oldPassword());
        validateLogin(loginRequest);

        user.setPassword(hashNewPassword);

        return UserResponse.fromEntity(userService.saveUser(user));
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
