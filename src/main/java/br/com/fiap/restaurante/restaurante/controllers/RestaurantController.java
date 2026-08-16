package br.com.fiap.restaurante.restaurante.controllers;

import br.com.fiap.restaurante.restaurante.services.RestaurantService;
import dtos.CreateRestaurantRequest;
import dtos.RestaurantResponse;
import dtos.UpdateRestaurantRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @Valid @RequestBody CreateRestaurantRequest request
    ) {
        return ResponseEntity.ok(
                restaurantService.createRestaurant(request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                restaurantService.findRestaurantById(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<RestaurantResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                restaurantService.findAllRestaurants(page, size)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRestaurantRequest request
    ) {
        return ResponseEntity.ok(
                restaurantService.updateRestaurant(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(
            @PathVariable Long id
    ) {
        restaurantService.deleteRestaurant(id);

        return ResponseEntity.noContent().build();
    }
}
