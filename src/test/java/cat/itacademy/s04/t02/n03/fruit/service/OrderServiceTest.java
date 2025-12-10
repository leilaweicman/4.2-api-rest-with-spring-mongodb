package cat.itacademy.s04.t02.n03.fruit.services;

import cat.itacademy.s04.t02.n03.fruit.dto.OrderCreateRequest;
import cat.itacademy.s04.t02.n03.fruit.dto.OrderItemRequest;
import cat.itacademy.s04.t02.n03.fruit.dto.OrderItemResponse;
import cat.itacademy.s04.t02.n03.fruit.dto.OrderResponse;
import cat.itacademy.s04.t02.n03.fruit.exception.BadRequestException;
import cat.itacademy.s04.t02.n03.fruit.model.Order;
import cat.itacademy.s04.t02.n03.fruit.model.OrderItem;
import cat.itacademy.s04.t02.n03.fruit.repository.OrderRepository;
import cat.itacademy.s04.t02.n03.fruit.service.OrderService;
import cat.itacademy.s04.t02.n03.fruit.service.OrderServiceImpl;
import cat.itacademy.s04.t02.n03.fruit.service.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, orderMapper);
    }

    @Test
    void createOrder_shouldSaveOrderAndReturnResponse() {

        OrderCreateRequest request = new OrderCreateRequest(
                "Alice",
                LocalDate.now().plusDays(1),
                List.of(new OrderItemRequest("Apple", 5))
        );

        Order toSave = new Order(null, "Alice", request.deliveryDate(),
                List.of(new OrderItem("Apple", 5)));

        Order saved = new Order("abc123", "Alice", request.deliveryDate(),
                List.of(new OrderItem("Apple", 5)));

        OrderResponse expected = new OrderResponse(
                "abc123",
                "Alice",
                request.deliveryDate(),
                List.of(new OrderItemResponse("Apple", 5))
        );

        when(orderMapper.toEntity(request)).thenReturn(toSave);
        when(orderRepository.save(toSave)).thenReturn(saved);
        when(orderMapper.toResponse(saved)).thenReturn(expected);

        OrderResponse response = orderService.createOrder(request);

        assertEquals("abc123", response.id());
        verify(orderRepository).save(toSave);
    }

    @Test
    void createOrder_shouldThrow_whenClientNameIsBlank() {

        OrderCreateRequest request = new OrderCreateRequest(
                "",
                LocalDate.now().plusDays(1),
                List.of(new OrderItemRequest("Apple", 5))
        );

        when(orderMapper.toEntity(request)).thenThrow(new BadRequestException("Client name cannot be empty"));

        assertThrows(BadRequestException.class, () -> orderService.createOrder(request));
    }

    @Test
    void createOrder_shouldThrow_whenDeliveryDateIsBeforeTomorrow() {

        OrderCreateRequest request = new OrderCreateRequest(
                "Alice",
                LocalDate.now(),
                List.of(new OrderItemRequest("Apple", 5))
        );

        when(orderMapper.toEntity(request)).thenThrow(
                new BadRequestException("Delivery date must be at least tomorrow")
        );

        assertThrows(BadRequestException.class, () -> orderService.createOrder(request));
    }

    @Test
    void createOrder_shouldThrow_whenItemsListIsEmpty() {

        OrderCreateRequest request = new OrderCreateRequest(
                "Alice",
                LocalDate.now().plusDays(1),
                List.of()
        );

        when(orderMapper.toEntity(request)).thenThrow(
                new BadRequestException("At least one item is required")
        );

        assertThrows(BadRequestException.class, () -> orderService.createOrder(request));
    }

    @Test
    void createOrder_shouldThrow_whenItemFruitNameIsBlank() {

        OrderCreateRequest request = new OrderCreateRequest(
                "Alice",
                LocalDate.now().plusDays(1),
                List.of(new OrderItemRequest("", 5))
        );

        when(orderMapper.toEntity(request)).thenThrow(
                new BadRequestException("Fruit name is required")
        );

        assertThrows(BadRequestException.class, () -> orderService.createOrder(request));
    }

    @Test
    void createOrder_shouldThrow_whenItemQuantityIsInvalid() {

        OrderCreateRequest request = new OrderCreateRequest(
                "Alice",
                LocalDate.now().plusDays(1),
                List.of(new OrderItemRequest("Apple", 0))
        );

        when(orderMapper.toEntity(request)).thenThrow(
                new BadRequestException("Quantity must be > 0")
        );

        assertThrows(BadRequestException.class, () -> orderService.createOrder(request));
    }

}

