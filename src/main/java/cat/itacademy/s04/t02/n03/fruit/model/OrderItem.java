package cat.itacademy.s04.t02.n03.fruit.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    private String fruitName;
    private int quantityInKilos;
}

