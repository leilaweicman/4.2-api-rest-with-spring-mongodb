package cat.itacademy.s04.t02.n03.fruit.valueobject;

import cat.itacademy.s04.t02.n03.fruit.exception.BadRequestException;

public record ClientName(String value) {

    public ClientName {
        if (value == null || value.isBlank()) {
            throw new BadRequestException("Client name cannot be empty");
        }
    }
}
