package br.com.fiap.restaurante.restaurante.services;

import br.com.fiap.restaurante.restaurante.entities.Restaurant;
import br.com.fiap.restaurante.restaurante.entities.User;
import br.com.fiap.restaurante.restaurante.repositories.RestaurantRepository;
import br.com.fiap.restaurante.restaurante.repositories.UserRepository;
import dtos.CreateRestaurantRequest;
import dtos.RestaurantResponse;
import dtos.UpdateRestaurantRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {

        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Restaurant restaurant = Restaurant.builder()
                .name(request.name())
                .description(request.description())
                .owner(owner)
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return toResponse(savedRestaurant);
    }

    public RestaurantResponse updateRestaurant(Long id, UpdateRestaurantRequest request) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        restaurant.setName(request.name());
        restaurant.setDescription(request.description());
        restaurant.setOwner(owner);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return toResponse(savedRestaurant);
    }

    public void deleteRestaurant(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        restaurantRepository.delete(restaurant);
    }

    public RestaurantResponse findRestaurantById(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        return toResponse(restaurant);
    }

    public Page<RestaurantResponse> findAllRestaurants(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return restaurantRepository.findAll(pageable)
                .map(this::toResponse);
    }

    private RestaurantResponse toResponse(Restaurant restaurant) {

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getOwner().getId()
        );
    }
}

