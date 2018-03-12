package hut34.wallet.client.transact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Response;

import java.io.IOException;

public class Web3jTransactionClient implements TransactionClient {
    private static final Logger LOG = LoggerFactory.getLogger(Web3jTransactionClient.class);

    private final Web3j web3j;

    public Web3jTransactionClient(Web3jServiceBuilder web3jServiceBuilder, @Value("${web3j.clientUrl}") String clientUrl) {
        this.web3j = Web3j.build(web3jServiceBuilder.build(clientUrl));
    }

    @Override
    public String sendSignedTransaction(String signedPayload) {
        return execute(() -> web3j.ethSendRawTransaction(signedPayload).send())
            .getTransactionHash();
    }


    private <T, R extends Response<T>> R execute(Executor<T, R> executor) {
        try {
            R response = executor.execute();
            validateResponse(response);
            return response;
        } catch (IOException e) {
            throw new TransactionClientException("Unexpected error executing transaction: " + e.getMessage(), e);
        }
    }

    private <T, R extends Response<T>> R validateResponse(R response) {
        Response.Error error = response.getError();
        if (error != null) {
            LOG.warn("Web3j remote error. [{}] {}. Data: {}", error.getCode(), error.getMessage(), error.getData());
            throw new TransactionClientException(String.format("Transaction error: [%s] %s", error.getCode(), error.getMessage()));
        }
        return response;
    }

    @FunctionalInterface
    private interface Executor<T, R extends Response<T>> {
        R execute() throws IOException;
    }

}
