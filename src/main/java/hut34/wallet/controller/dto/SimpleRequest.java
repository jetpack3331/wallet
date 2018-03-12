package hut34.wallet.controller.dto;

public class SimpleRequest<T> {

    private T value;

    public SimpleRequest() {
    }

    public SimpleRequest(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
