package br.com.fiap.restaurante.restaurante.repositories;

import br.com.fiap.restaurante.restaurante.entities.User;
import dtos.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);

    List<User> findByNameContainingIgnoreCase(String name);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);
}
