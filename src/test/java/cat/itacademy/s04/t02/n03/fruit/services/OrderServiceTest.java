package cat.itacademy.s04.t02.n03.fruit.services;

import cat.itacademy.s04.t02.n03.fruit.dto.OrderCreateRequest;
import cat.itacademy.s04.t02.n03.fruit.dto.OrderItemRequest;
import cat.itacademy.s04.t02.n03.fruit.dto.OrderResponse;
import cat.itacademy.s04.t02.n03.fruit.model.Order;
import cat.itacademy.s04.t02.n03.fruit.model.OrderItem;
import cat.itacademy.s04.t02.n03.fruit.repository.OrderRepository;
import cat.itacademy.s04.t02.n03.fruit.service.OrderService;
import cat.itacademy.s04.t02.n03.fruit.service.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository);
    }

    @Test
    void createOrder_shouldSaveOrderAndReturnResponse() {

        OrderCreateRequest request = new OrderCreateRequest(
                "Alice",
                LocalDate.now().plusDays(1),
                List.of(new OrderItemRequest("Apple", 5))
        );

        Order savedEntity = new Order("abc123", "Alice", request.deliveryDate(), List.of(new OrderItem("Apple", 5)));

        when(orderRepository.save(any(Order.class))).thenReturn(savedEntity);

        OrderResponse response = orderService.createOrder(request);

        assertThat(response.id()).isEqualTo("abc123");
        assertThat(response.clientName()).isEqualTo("Alice");
        assertThat(response.items().get(0).fruitName()).isEqualTo("Apple");
        assertThat(response.items().get(0).quantityInKilos()).isEqualTo(5);

        verify(orderRepository).save(any(Order.class));
    }
}

