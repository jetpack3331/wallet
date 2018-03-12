package hut34.wallet.controller.dto;

public class SimpleResponse<T> {

    private final T result;

    public SimpleResponse(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }
}
