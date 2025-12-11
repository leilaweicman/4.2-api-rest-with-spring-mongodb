package cat.itacademy.s04.t02.n03.fruit.service;

import cat.itacademy.s04.t02.n03.fruit.dto.OrderCreateRequest;
import cat.itacademy.s04.t02.n03.fruit.dto.OrderResponse;
import cat.itacademy.s04.t02.n03.fruit.exception.NotFoundException;
import cat.itacademy.s04.t02.n03.fruit.model.Order;
import cat.itacademy.s04.t02.n03.fruit.model.OrderItem;
import cat.itacademy.s04.t02.n03.fruit.repository.OrderRepository;
import cat.itacademy.s04.t02.n03.fruit.service.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper mapper) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public OrderResponse createOrder(OrderCreateRequest request) {
        Order order = mapper.toEntity(request);
        Order saved = orderRepository.save(order);

        return mapper.toResponse(saved);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public OrderResponse getOrderById(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        return mapper.toResponse(order);
    }

    @Override
    public OrderResponse updateOrder(String id, OrderCreateRequest request) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        existing.setClientName(request.clientName());
        existing.setDeliveryDate(request.deliveryDate());

        List<OrderItem> items = request.items().stream()
                .map(i -> new OrderItem(i.fruitName(), i.quantityInKilos()))
                .toList();

        existing.setItems(items);

        Order saved = orderRepository.save(existing);

        return mapper.toResponse(saved);
    }

    @Override
    public void deleteOrder(String id) {

    }

}

