package cat.itacademy.s04.t02.n03.fruit.service;

import cat.itacademy.s04.t02.n03.fruit.dto.*;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest request);
    List<OrderResponse> getAllOrders();
}
