package br.com.fiap.restaurante.restaurante.repositories;

import br.com.fiap.restaurante.restaurante.entities.Restaurant;
import br.com.fiap.restaurante.restaurante.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByOwner(User owner);

    List<Restaurant> findByNameContainingIgnoreCase(String name);
}
