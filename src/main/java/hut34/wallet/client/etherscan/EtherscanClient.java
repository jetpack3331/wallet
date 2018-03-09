package hut34.wallet.client.etherscan;

import com.google.common.collect.Sets;
import hut34.wallet.client.etherscan.model.Response;
import hut34.wallet.client.etherscan.model.StringResponse;
import hut34.wallet.client.etherscan.model.Transaction;
import hut34.wallet.client.etherscan.model.TransactionListResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.List;
import java.util.Set;

@Component
public class EtherscanClient {
    private static final String OK_STATUS = "1";
    private static final Set<String> ERRORS_TO_IGNORE = Sets.newHashSet("No transactions found");

    private final RestOperations restOperations;
    private final String baseUrl;

    public EtherscanClient(RestTemplateBuilder restTemplateBuilder, @Value("${etherscan.apiKey}") String apiKey) {
        this.restOperations = restTemplateBuilder.build();
        this.baseUrl = String.format("https://api.etherscan.io/api?apikey=%s&", apiKey);
    }

    public String getBalance(String address) {
        StringResponse response = restOperations
            .getForObject(baseUrl + "module={module}&action={action}&address={address}", StringResponse.class, "account", "balance", address);

        validateResponse(response);

        return response.getResult();
    }

    public List<Transaction> getTransactions(String address, Sort sort) {
        TransactionListResponse response = restOperations
            .getForObject(baseUrl + "module={module}&action={action}&address={address}&sort={sort}", TransactionListResponse.class, "account", "txlist", address, sort.paramValue());

        validateResponse(response);

        return response.getResult();
    }

    private <T> Response<T> validateResponse(Response<T> response) {
        if (!OK_STATUS.equals(response.getStatus()) && !ERRORS_TO_IGNORE.contains(response.getMessage())) {
            throw new EtherscanClientException(String.format("Error from remote server: %s", response.getMessage()));
        }
        return response;
    }


}
