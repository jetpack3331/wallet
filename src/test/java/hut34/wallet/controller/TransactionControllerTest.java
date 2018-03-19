package hut34.wallet.controller;

import hut34.wallet.client.gas.GasClient;
import hut34.wallet.client.gas.GasInfo;
import hut34.wallet.client.transact.TransactionClient;
import hut34.wallet.controller.dto.CreateTransactionRequest;
import hut34.wallet.service.ManagedAccountService;
import hut34.wallet.testinfra.BaseControllerTest;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest extends BaseControllerTest {

    @Mock
    private TransactionClient transactionClient;
    @Mock
    private GasClient gasClient;
    @Mock
    private ManagedAccountService managedAccountService;

    @Override
    protected Object controller() {
        return new TransactionController(transactionClient, gasClient, managedAccountService);
    }

    @Test
    public void submitSignedTransaction() throws Exception {
        when(transactionClient.sendSignedTransaction("signedTransaction")).thenReturn("transactionHash");

        mvc.perform(
            post("/api/transactions")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"value\": \"signedTransaction\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("result", is("transactionHash")));
    }

    @Test
    public void signTransaction() throws Exception {
        when(managedAccountService.loadCredentials("0xFROM")).thenReturn(Credentials.create(Keys.createEcKeyPair()));
        CreateTransactionRequest txnRequest = validTransactionRequest();

        mvc.perform(
            post("/api/transactions/sign")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asString(txnRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("result", any(String.class)));
    }

    @Test
    public void signTransaction_willFailValidationIfFromAddressMissing() throws Exception {
        CreateTransactionRequest request = validTransactionRequest().setFrom(null);
        expectBadRequest("/api/transactions/sign", request);
    }

    @Test
    public void signTransaction_willFailValidationIfToAddressMissing() throws Exception {
        CreateTransactionRequest request = validTransactionRequest().setTo(null);
        expectBadRequest("/api/transactions/sign", request);
    }

    @Test
    public void signTransaction_willFailValidationIfNonceMissing() throws Exception {
        CreateTransactionRequest request = validTransactionRequest().setNonce(null);
        expectBadRequest("/api/transactions/sign", request);
    }

    @Test
    public void signTransaction_willFailValidationIfGasLimitMissing() throws Exception {
        CreateTransactionRequest request = validTransactionRequest().setGasLimit(null);
        expectBadRequest("/api/transactions/sign", request);
    }

    @Test
    public void signTransaction_willFailValidationIfGasPriceMissing() throws Exception {
        CreateTransactionRequest request = validTransactionRequest().setGasPrice(null);
        expectBadRequest("/api/transactions/sign", request);
    }

    @Test
    public void signTransaction_willFailValidationIfValueMissing() throws Exception {
        CreateTransactionRequest request = validTransactionRequest().setValue(null);
        expectBadRequest("/api/transactions/sign", request);
    }

    @Test
    public void getGasInfo() throws Exception {
        GasInfo gasInfo = new GasInfo();
        gasInfo.setAverage(new BigDecimal("10"));
        gasInfo.setSafeLow(new BigDecimal("20"));
        gasInfo.setFast(new BigDecimal("30"));

        when(gasClient.getGasInfo()).thenReturn(gasInfo);

        mvc.perform(
            get("/api/gas").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("averagePrice", is("1000000000")))
            .andExpect(jsonPath("safeLowPrice", is("2000000000")))
            .andExpect(jsonPath("fastPrice", is("3000000000")));
    }

    @Test
    public void getNextNonce() throws Exception {
        when(transactionClient.getNextNonce("address")).thenReturn(new BigInteger("99"));

        mvc.perform(
            get("/api/accounts/{address}/nonce", "address").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("result", is(99)));
    }

    private void expectBadRequest(String url, Object content) throws Exception {
        mvc.perform(
            post(url)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asString(content)))
            .andExpect(status().isBadRequest());
    }

    private CreateTransactionRequest validTransactionRequest() {
        return new CreateTransactionRequest()
            .setFrom("0xFROM")
            .setTo("0xTO")
            .setNonce("1")
            .setGasLimit("21000")
            .setGasPrice("2000000000")
            .setValue("100000000000000");
    }
}
