package cat.itacademy.s04.t02.n03.fruit.dto;

import java.time.LocalDate;
import java.util.List;

public record OrderResponse(
        String id,
        String clientName,
        LocalDate deliveryDate,
        List<OrderItemResponse> items
) {}

