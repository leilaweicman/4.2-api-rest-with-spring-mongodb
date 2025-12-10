package cat.itacademy.s04.t02.n03.fruit.dto;

import jakarta.validation.constraints.*;

public record OrderItemRequest(
        @NotBlank(message = "Fruit name is required")
        String fruitName,

        @Positive(message = "Quantity must be greater than 0")
        Integer quantityInKilos
) {}

