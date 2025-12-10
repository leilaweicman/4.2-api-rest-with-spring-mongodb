package cat.itacademy.s04.t02.n03.fruit.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record OrderCreateRequest(
        @NotBlank(message = "Client name is required")
        String clientName,

        @NotNull(message = "Delivery date is required")
        LocalDate deliveryDate,

        @Size(min = 1, message = "At least one item is required")
        List<@Valid OrderItemRequest> items
) {}

