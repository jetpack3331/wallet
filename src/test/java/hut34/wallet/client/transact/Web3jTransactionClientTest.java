package hut34.wallet.client.transact;

import hut34.wallet.testinfra.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class Web3jTransactionClientTest extends BaseTest {
    private static final String CLIENT_URL = "https://client-url";

    @Mock
    private Web3jServiceBuilder web3jServiceBuilder;
    @Mock
    private Web3jService web3jService;

    private Web3jTransactionClient client;

    @Before
    public void before() {
        when(web3jServiceBuilder.build(CLIENT_URL)).thenReturn(web3jService);
        client = new Web3jTransactionClient(web3jServiceBuilder, CLIENT_URL);
    }

    @Test
    public void sendSignedTransaction() throws IOException {
        EthSendTransaction response = new EthSendTransaction();
        response.setResult("transaction-hash");
        when(web3jService.send(any(Request.class), eq(EthSendTransaction.class))).thenReturn(response);

        String result = client.sendSignedTransaction("signedPayload");

        assertThat(result, is("transaction-hash"));
    }

    @Test
    public void sendSignedTransaction_willError_whenIOExceptionEncountered() throws IOException {
        when(web3jService.send(any(Request.class), eq(EthSendTransaction.class))).thenThrow(new IOException("some error"));

        thrown.expect(TransactionClientException.class);
        thrown.expectMessage("Unexpected error executing transaction: some error");

        client.sendSignedTransaction("signedPayload");
    }

    @Test
    public void sendSignedTransaction_willError_whenResponseContainsError() throws IOException {
        EthSendTransaction response = new EthSendTransaction();
        response.setResult("transaction-hash");
        response.setError(new Response.Error(123, "You got yourself an error"));
        when(web3jService.send(any(Request.class), eq(EthSendTransaction.class))).thenReturn(response);

        thrown.expect(TransactionClientException.class);
        thrown.expectMessage("Transaction error: [123] You got yourself an error");

        client.sendSignedTransaction("signedPayload");
    }
    

}
