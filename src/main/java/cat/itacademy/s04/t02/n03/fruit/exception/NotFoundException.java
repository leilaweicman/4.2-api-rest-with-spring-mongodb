package cat.itacademy.s04.t02.n03.fruit.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

