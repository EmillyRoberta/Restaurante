package br.com.fiap.restaurante.restaurante.controllers;

import br.com.fiap.restaurante.restaurante.controllers.types.HttpStatusCode;
import br.com.fiap.restaurante.restaurante.services.RestaurantService;
import dtos.CreateRestaurantRequest;
import dtos.RestaurantResponse;
import dtos.UpdateRestaurantRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Operation(description = "Creates new restaurant. Owner must be valid",
            summary = "Creates new restaurant.",
            responses = {
                    @ApiResponse(description = "Restaurant created successfully.", responseCode = HttpStatusCode.CREATED),
                    @ApiResponse(description = "Owner not found.", responseCode = HttpStatusCode.NOT_FOUND),
                    @ApiResponse(description = "User is not a restaurant owner.", responseCode = HttpStatusCode.UNPROCESSABLE_CONTENT,
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @Valid @RequestBody CreateRestaurantRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.createRestaurant(request));
    }

    @Operation(description = "Lists restaurant by ID.",
            summary = "Lists restaurant by ID.")
    @ApiResponses(value = {
            @ApiResponse(description = "Success.", responseCode = HttpStatusCode.OK),
            @ApiResponse(description = "Restaurant not found.", responseCode = HttpStatusCode.NOT_FOUND)
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                restaurantService.findRestaurantById(id)
        );
    }

    @Operation(description = "Lists all restaurants.",
            summary = "Lists all restaurants.")
    @ApiResponses(value = {
            @ApiResponse(description = "Success.", responseCode = HttpStatusCode.OK)
    }
    )
    @GetMapping
    public ResponseEntity<Page<RestaurantResponse>> findAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        return ResponseEntity.ok(
                restaurantService.findAllRestaurants(page, size)
        );
    }

    @Operation(description = "Updates restaurant data. Owner must be valid.",
            summary = "Updates restaurant data.",
            responses = {
                    @ApiResponse(description = "Restaurant updated successfully.", responseCode = HttpStatusCode.OK),
                    @ApiResponse(description = "Restaurant or Owner not found.", responseCode = HttpStatusCode.NOT_FOUND),
                    @ApiResponse(description = "User is not a restaurant owner.", responseCode = HttpStatusCode.UNPROCESSABLE_CONTENT,
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRestaurantRequest request
    ) {
        return ResponseEntity.ok(
                restaurantService.updateRestaurant(id, request)
        );
    }

    @Operation(description = "Deletes restaurant.",
            summary = "Deletes restaurant.",
            responses = {
                    @ApiResponse(description = "Restaurant deleted successfully.", responseCode = HttpStatusCode.OK),
                    @ApiResponse(description = "Restaurant not found.", responseCode = HttpStatusCode.NOT_FOUND)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(
            @PathVariable @Positive @NotNull Long id
    ) {
        restaurantService.deleteRestaurant(id);

        return ResponseEntity.ok().build();
    }
}
