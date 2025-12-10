package cat.itacademy.s04.t02.n03.fruit.model;

public class OrderItem {

    private String fruitName;
    private int quantityInKilos;

    public OrderItem() {}

    public OrderItem(String fruitName, int quantityInKilos) {
        this.fruitName = fruitName;
        this.quantityInKilos = quantityInKilos;
    }

    public String getFruitName() { return fruitName; }
    public int getQuantityInKilos() { return quantityInKilos; }
}

