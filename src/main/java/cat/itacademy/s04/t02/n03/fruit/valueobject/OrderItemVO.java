package cat.itacademy.s04.t02.n03.fruit.valueobject;

import cat.itacademy.s04.t02.n03.fruit.exception.BadRequestException;
import cat.itacademy.s04.t02.n03.fruit.model.OrderItem;

public record OrderItemVO(String fruitName, int quantityInKilos) {

    public OrderItemVO {
        if (fruitName == null || fruitName.isBlank()) {
            throw new BadRequestException("Fruit name is required");
        }
        if (quantityInKilos <= 0) {
            throw new BadRequestException("Quantity must be > 0");
        }
    }

    public OrderItem toEntity() {
        return new OrderItem(fruitName, quantityInKilos);
    }
}

