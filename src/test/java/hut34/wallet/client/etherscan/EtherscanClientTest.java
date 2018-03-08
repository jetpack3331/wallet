package hut34.wallet.client.etherscan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hut34.wallet.client.etherscan.model.ListResponse;
import hut34.wallet.client.etherscan.model.StringResponse;
import hut34.wallet.client.etherscan.model.Transaction;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.Arrays;
import java.util.List;

import static hut34.wallet.client.etherscan.TestEtherscan.ONE_ETH;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@RestClientTest(EtherscanClient.class)
public class EtherscanClientTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Autowired
    private EtherscanClient client;
    @Autowired
    private MockRestServiceServer server;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getBalance() {
        server.expect(requestTo("https://api.etherscan.io/api?apikey=test-etherscan-key&module=account&action=balance&address=my-address"))
            .andRespond(withSuccess(stringResponseSuccess(ONE_ETH), MediaType.APPLICATION_JSON));

        String balance = client.getBalance("my-address");

        assertThat(balance, is(ONE_ETH));
    }

    @Test
    public void getBalance_willError_whenStatusIsNot_1() {
        StringResponse response = new StringResponse();
        response.setStatus("0");
        response.setMessage("Some error happened");

        server.expect(requestTo("https://api.etherscan.io/api?apikey=test-etherscan-key&module=account&action=balance&address=my-address"))
            .andRespond(withSuccess(asString(response), MediaType.APPLICATION_JSON));

        thrown.expect(EtherscanClientException.class);
        thrown.expectMessage("Error from remote server: Some error happened");
        client.getBalance("my-address");
    }

    @Test
    public void getTransactions() {
        List<Transaction> expected = Arrays.asList(TestEtherscan.transaction(), TestEtherscan.transactionError());

        server.expect(requestTo("https://api.etherscan.io/api?apikey=test-etherscan-key&module=account&action=txlist&address=my-address&sort=asc"))
            .andRespond(withSuccess(listResponseSuccess(expected), MediaType.APPLICATION_JSON));

        List<Transaction> result = client.getTransactions("my-address", Sort.ASC);

        assertThat(result, equalTo(expected));
    }

    @Test
    public void getTransactions_descending() {
        List<Transaction> expected = Arrays.asList(TestEtherscan.transaction(), TestEtherscan.transactionError());

        server.expect(requestTo("https://api.etherscan.io/api?apikey=test-etherscan-key&module=account&action=txlist&address=my-address&sort=desc"))
            .andRespond(withSuccess(listResponseSuccess(expected), MediaType.APPLICATION_JSON));

        List<Transaction> result = client.getTransactions("my-address", Sort.DESC);

        assertThat(result, equalTo(expected));
    }

    @Test
    public void getTransactions_willError_whenStatusIsNot_1() {
        ListResponse<Transaction> response = new ListResponse<>();
        response.setStatus("0");
        response.setMessage("Some error happened");

        server.expect(requestTo("https://api.etherscan.io/api?apikey=test-etherscan-key&module=account&action=txlist&address=my-address&sort=asc"))
            .andRespond(withSuccess(asString(response), MediaType.APPLICATION_JSON));

        thrown.expect(EtherscanClientException.class);
        thrown.expectMessage("Error from remote server: Some error happened");
        client.getTransactions("my-address", Sort.ASC);
    }

    private <T> String listResponseSuccess(List<T> response) {
        ListResponse<T> listResponse = new ListResponse<>();
        listResponse.setStatus("1");
        listResponse.setMessage("OK");
        listResponse.setResponse(response);
        return asString(listResponse);
    }

    private String stringResponseSuccess(String value) {
        StringResponse stringResponse = new StringResponse();
        stringResponse.setStatus("1");
        stringResponse.setMessage("OK");
        stringResponse.setResponse(value);
        return asString(stringResponse);
    }

    private String asString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
