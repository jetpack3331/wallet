package hut34.wallet.util;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("Not found");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
