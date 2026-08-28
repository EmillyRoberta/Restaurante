package br.com.fiap.restaurante.restaurante.services;

import br.com.fiap.restaurante.restaurante.entities.Restaurant;
import br.com.fiap.restaurante.restaurante.entities.User;
import br.com.fiap.restaurante.restaurante.entities.UserType;
import br.com.fiap.restaurante.restaurante.repositories.RestaurantRepository;
import br.com.fiap.restaurante.restaurante.repositories.UserRepository;
import br.com.fiap.restaurante.restaurante.services.exceptions.BusinessException;
import br.com.fiap.restaurante.restaurante.services.exceptions.ResourceNotFoundException;
import dtos.CreateRestaurantRequest;
import dtos.RestaurantResponse;
import dtos.UpdateRestaurantRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {

        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        if( owner.getUserType() != UserType.RESTAURANT_OWNER) {
            throw new BusinessException("User is not a restaurant owner");
        }

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
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        restaurant.setName(request.name());
        restaurant.setDescription(request.description());
        restaurant.setOwner(owner);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return toResponse(savedRestaurant);
    }

    public void deleteRestaurant(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        restaurantRepository.delete(restaurant);
    }

    public RestaurantResponse findRestaurantById(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

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

    public List<RestaurantResponse> findByOwner(User owner) {
        return restaurantRepository.findByOwner(owner)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RestaurantResponse> findByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponse)
                .toList();
    }
}

