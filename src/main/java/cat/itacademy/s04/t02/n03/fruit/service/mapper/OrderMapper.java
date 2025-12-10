package cat.itacademy.s04.t02.n03.fruit.service.mapper;

import cat.itacademy.s04.t02.n03.fruit.dto.OrderCreateRequest;
import cat.itacademy.s04.t02.n03.fruit.dto.OrderItemResponse;
import cat.itacademy.s04.t02.n03.fruit.dto.OrderResponse;
import cat.itacademy.s04.t02.n03.fruit.model.Order;
import cat.itacademy.s04.t02.n03.fruit.model.OrderItem;
import cat.itacademy.s04.t02.n03.fruit.valueobject.ClientName;
import cat.itacademy.s04.t02.n03.fruit.valueobject.DeliveryDate;
import cat.itacademy.s04.t02.n03.fruit.valueobject.OrderItemVO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public Order toEntity(OrderCreateRequest request) {

        ClientName clientName = new ClientName(request.clientName());
        DeliveryDate deliveryDate = new DeliveryDate(request.deliveryDate());

        List<OrderItem> items = request.items().stream()
                .map(item -> new OrderItemVO(item.fruitName(), item.quantityInKilos()).toEntity())
                .toList();

        return Order.builder()
                .clientName(clientName.value())
                .deliveryDate(deliveryDate.value())
                .items(items)
                .build();
    }


    public OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items = order.getItems().stream()
                .map(i -> new OrderItemResponse(i.getFruitName(), i.getQuantityInKilos()))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getClientName(),
                order.getDeliveryDate(),
                items
        );
    }
}

