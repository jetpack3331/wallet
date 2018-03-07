package hut34.wallet.client.etherscan;

public class EtherscanClientException extends RuntimeException {

    public EtherscanClientException(String message) {
        super(message);
    }

    public EtherscanClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
