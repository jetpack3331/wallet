package hut34.wallet.framework.controller.advice;

public class ResponseError {
    private final String error;
    private final String message;

    public ResponseError(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

}
