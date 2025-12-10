package cat.itacademy.s04.t02.n03.fruit.service;

import cat.itacademy.s04.t02.n03.fruit.dto.OrderCreateRequest;
import cat.itacademy.s04.t02.n03.fruit.dto.OrderItemResponse;
import cat.itacademy.s04.t02.n03.fruit.dto.OrderResponse;
import cat.itacademy.s04.t02.n03.fruit.model.Order;
import cat.itacademy.s04.t02.n03.fruit.model.OrderItem;
import cat.itacademy.s04.t02.n03.fruit.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponse createOrder(OrderCreateRequest request) {

        List<OrderItem> items = request.items().stream()
                .map(i -> new OrderItem(i.fruitName(), i.quantityInKilos()))
                .toList();

        Order orderToSave = new Order(
                request.clientName(),
                request.deliveryDate(),
                items
        );

        Order saved = orderRepository.save(orderToSave);

        List<OrderItemResponse> itemResponses = saved.getItems().stream()
                .map(i -> new OrderItemResponse(i.getFruitName(), i.getQuantityInKilos()))
                .toList();

        return new OrderResponse(
                saved.getId(),
                saved.getClientName(),
                saved.getDeliveryDate(),
                itemResponses
        );
    }
}

