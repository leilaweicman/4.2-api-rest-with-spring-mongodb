package cat.itacademy.s04.t02.n03.fruit.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
    void createOrder_shouldReturn400_whenQuantityInvalid() throws Exception {

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

}

