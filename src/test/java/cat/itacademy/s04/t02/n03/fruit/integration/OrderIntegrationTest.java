package cat.itacademy.s04.t02.n03.fruit.integration;

import cat.itacademy.s04.t02.n03.fruit.dto.OrderCreateRequest;
import cat.itacademy.s04.t02.n03.fruit.dto.OrderItemRequest;
import cat.itacademy.s04.t02.n03.fruit.model.Order;
import cat.itacademy.s04.t02.n03.fruit.model.OrderItem;
import cat.itacademy.s04.t02.n03.fruit.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void cleanDb() {
        orderRepository.deleteAll();
    }

    @Test
    void createOrder_returnsCreated_AndSavedOrder() throws Exception {

        String body = """
                {
                  "clientName": "Alice",
                  "deliveryDate": "%s",
                  "items": [
                    { "fruitName": "Apple", "quantityInKilos": 5 }
                  ]
                }
                """.formatted(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.clientName").value("Alice"))
                .andExpect(jsonPath("$.items[0].fruitName").value("Apple"))
                .andExpect(jsonPath("$.items[0].quantityInKilos").value(5));
    }

    @Test
    void createOrder_returnsBadRequest_whenClientNameIsBlank() throws Exception {

        String body = """
            {
              "clientName": "",
              "deliveryDate": "%s",
              "items": [
                { "fruitName": "Apple", "quantityInKilos": 5 }
              ]
            }
            """.formatted(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createOrder_returnsBadRequest_whenDeliveryDateIsInPast() throws Exception {

        String body = """
            {
              "clientName": "Alice",
              "deliveryDate": "%s",
              "items": [
                { "fruitName": "Apple", "quantityInKilos": 5 }
              ]
            }
            """.formatted(LocalDate.now());

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Delivery date must be at least tomorrow"));
    }

    @Test
    void createOrder_returnsBadRequest_whenItemsIsEmpty() throws Exception {

        String body = """
            {
              "clientName": "Alice",
              "deliveryDate": "%s",
              "items": []
            }
            """.formatted(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createOrder_returnsBadRequest_whenQuantityInvalid() throws Exception {

        String body = """
            {
              "clientName": "Alice",
              "deliveryDate": "%s",
              "items": [
                { "fruitName": "Apple", "quantityInKilos": 0 }
              ]
            }
            """.formatted(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Quantity must be > 0"));
    }

    @Test
    void getAllOrders_returnsEmptyList_whenNoOrdersExist() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAllOrders_returnsListOfOrders_whenOrdersExist() throws Exception {

        Order order1 = new Order("John", LocalDate.now().plusDays(1),
                List.of(new OrderItem("Apple", 2)));
        orderRepository.save(order1);

        Order order2 = new Order("Anna", LocalDate.now().plusDays(2),
                List.of(new OrderItem("Banana", 3)));
        orderRepository.save(order2);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].clientName").value("John"))
                .andExpect(jsonPath("$[1].clientName").value("Anna"));
    }

    @Test
    void getOrderById_returnsOrder_whenIdExists() throws Exception {
        Order order = new Order("John", LocalDate.now().plusDays(1),
                List.of(new OrderItem("Apple", 2)));
        order = orderRepository.save(order);

        mockMvc.perform(get("/orders/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.clientName").value("John"))
                .andExpect(jsonPath("$.items[0].fruitName").value("Apple"))
                .andExpect(jsonPath("$.items[0].quantityInKilos").value(2));
    }

    @Test
    void getOrderById_returnsNotFound_whenIdNotExists() throws Exception {
        mockMvc.perform(get("/orders/unknown-id-123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void updateOrder_returnsUpdatedOrder_whenIdExists() throws Exception {

        Order existing = new Order("John", LocalDate.now().plusDays(1),
                List.of(new OrderItem("Apple", 2)));
        existing = orderRepository.save(existing);

        String json = """
            {
              "clientName": "Updated Client",
              "deliveryDate": "%s",
              "items": [
                {"fruitName": "Orange", "quantityInKilos": 5}
              ]
            }
            """.formatted(LocalDate.now().plusDays(2));

        mockMvc.perform(put("/orders/" + existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("Updated Client"))
                .andExpect(jsonPath("$.items[0].fruitName").value("Orange"))
                .andExpect(jsonPath("$.items[0].quantityInKilos").value(5));
    }

    @Test
    void updateOrder_returnsNotFound_whenIdNotExists() throws Exception {

        String json = """
            {
              "clientName": "Client",
              "deliveryDate": "%s",
              "items": [
                {"fruitName": "Banana", "quantityInKilos": 2}
              ]
            }
            """.formatted(LocalDate.now().plusDays(1));

        mockMvc.perform(put("/orders/unknown-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

}

