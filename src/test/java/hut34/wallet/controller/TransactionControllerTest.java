package hut34.wallet.controller;

import hut34.wallet.client.gas.GasClient;
import hut34.wallet.client.gas.GasInfo;
import hut34.wallet.client.transact.TransactionClient;
import hut34.wallet.testinfra.BaseControllerTest;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.math.BigInteger;

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

    @Override
    protected Object controller() {
        return new TransactionController(transactionClient, gasClient);
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

}
