package cat.itacademy.s04.t02.n03.fruit.valueobject;

import cat.itacademy.s04.t02.n03.fruit.exception.BadRequestException;

import java.time.LocalDate;

public record DeliveryDate(LocalDate value) {

    public DeliveryDate {
        if (value == null || value.isBefore(LocalDate.now().plusDays(1))) {
            throw new BadRequestException("Delivery date must be at least tomorrow");
        }
    }
}

