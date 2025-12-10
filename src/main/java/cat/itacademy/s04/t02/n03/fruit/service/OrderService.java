package cat.itacademy.s04.t02.n03.fruit.service;

import cat.itacademy.s04.t02.n03.fruit.dto.*;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest request);
}
