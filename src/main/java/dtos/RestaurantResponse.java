package dtos;

public record RestaurantResponse(
        Long id,
        String name,
        String description,
        Long ownerId
) {
}
