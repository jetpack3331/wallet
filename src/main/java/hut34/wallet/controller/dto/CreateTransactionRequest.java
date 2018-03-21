package hut34.wallet.controller.dto;

import org.hibernate.validator.constraints.NotBlank;

public class CreateTransactionRequest {

    @NotBlank
    private String from;
    @NotBlank
    private String to;
    @NotBlank
    private String nonce;
    @NotBlank
    private String gasPrice;
    @NotBlank
    private String gasLimit;
    @NotBlank
    private String value;
    private String data;

    public CreateTransactionRequest() {
    }

    public String getFrom() {
        return from;
    }

    public CreateTransactionRequest setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public CreateTransactionRequest setTo(String to) {
        this.to = to;
        return this;
    }

    public String getNonce() {
        return nonce;
    }

    public CreateTransactionRequest setNonce(String nonce) {
        this.nonce = nonce;
        return this;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public CreateTransactionRequest setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
        return this;
    }

    public String getGasLimit() {
        return gasLimit;
    }

    public CreateTransactionRequest setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit;
        return this;
    }

    public String getValue() {
        return value;
    }

    public CreateTransactionRequest setValue(String value) {
        this.value = value;
        return this;
    }

    public String getData() {
        return data;
    }

    public CreateTransactionRequest setData(String data) {
        this.data = data;
        return this;
    }
}
