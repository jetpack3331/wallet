package hut34.wallet.controller;

import hut34.wallet.client.etherscan.EtherscanClient;
import hut34.wallet.client.etherscan.Sort;
import hut34.wallet.client.etherscan.TestEtherscan;
import hut34.wallet.client.etherscan.model.Transaction;
import hut34.wallet.controller.dto.CreateWalletRequest;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.service.WalletAccountService;
import hut34.wallet.testinfra.BaseControllerTest;
import hut34.wallet.testinfra.TestData;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;

import static hut34.wallet.client.etherscan.TestEtherscan.ONE_ETH;
import static hut34.wallet.model.WalletAccountType.PRIVATE;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WalletAccountControllerTest extends BaseControllerTest {

    private static final String ETHEREUM_ADDRESS = "0x236cBCDf103a77F6401cF40cAc1F22Ca80B7aaE4";
    @Mock
    private WalletAccountService walletAccountService;
    @Mock
    private EtherscanClient etherscanClient;

    @Override
    protected Object controller() {
        return new WalletAccountController(walletAccountService, etherscanClient);
    }

    @Test
    public void create() throws Exception {
        CreateWalletRequest request = new CreateWalletRequest(PRIVATE, ETHEREUM_ADDRESS, "femfefiefowejfno43ifnm34ofin3498f3498598");
        WalletAccount walletAccount = new WalletAccount(PRIVATE, request.getAddress(), TestData.user())
            .setSecretStorageJson(request.getSecretStorageJson());
        when(walletAccountService.createPrivate(request.getAddress(), request.getSecretStorageJson())).thenReturn(walletAccount);

        mvc.perform(
            post("/api/wallets/accounts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("address", is(request.getAddress())))
            .andExpect(jsonPath("secretStorageJson", is(request.getSecretStorageJson())));
    }

    @Test
    public void listForCurrentUser() throws Exception {
        WalletAccount walletAccount = TestData.walletAccount();
        when(walletAccountService.listForCurrentUser()).thenReturn(Collections.singletonList(walletAccount));

        mvc.perform(
            get("/api/wallets/accounts/mine").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("[0].address", is(walletAccount.getAddress())))
            .andExpect(jsonPath("[0].secretStorageJson", is(walletAccount.getSecretStorageJson())));
    }

    @Test
    public void getBalance() throws Exception {
        when(etherscanClient.getBalance(ETHEREUM_ADDRESS)).thenReturn(ONE_ETH);

        mvc.perform(
            get("/api/wallets/accounts/{address}/balance", ETHEREUM_ADDRESS).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("address", is(ETHEREUM_ADDRESS)))
            .andExpect(jsonPath("balance", is(ONE_ETH)));
    }

    @Test
    public void getTokenBalance() throws Exception {
        when(etherscanClient.getTokenBalance("contract-address", ETHEREUM_ADDRESS)).thenReturn("9700000000000000000");

        mvc.perform(
            get("/api/wallets/accounts/{address}/tokens/{contractAddress}/balance", ETHEREUM_ADDRESS, "contract-address").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("contractAddress", is("contract-address")))
            .andExpect(jsonPath("address", is(ETHEREUM_ADDRESS)))
            .andExpect(jsonPath("balance", is("9700000000000000000")));
    }

    @Test
    public void getTransactions() throws Exception {
        Transaction transaction = TestEtherscan.transaction();
        when(etherscanClient.getTransactions(ETHEREUM_ADDRESS, Sort.DESC)).thenReturn(Collections.singletonList(transaction));

        mvc.perform(
            get("/api/wallets/accounts/{address}/transactions", ETHEREUM_ADDRESS).contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("address", is(ETHEREUM_ADDRESS)))
            .andExpect(jsonPath("transactions", hasSize(1)))
            .andExpect(jsonPath("transactions[0].hash", is(transaction.getHash())));
    }

    @Test
    public void download_willReturn404_whenWalletNotFound() throws Exception {
        when(walletAccountService.get("WalletAddress")).thenReturn(Optional.empty());

        mvc.perform(
            get("/api/wallets/accounts/WalletAddress/download"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("error", is("NotFoundException")))
            .andExpect(jsonPath("message", is("Not found")));
    }

    @Test
    public void download_willDownloadSecretStorageJson() throws Exception {
        WalletAccount walletAccount = TestData.walletAccount();
        ReflectionTestUtils.setField(walletAccount, "created", OffsetDateTime.parse("2018-03-07T09:28:10.445Z"));
        when(walletAccountService.get("0xASDFMEWIFREVNERIGVNERTIGRTNBRT")).thenReturn(Optional.of(walletAccount));
        String expectedFilename = "UTC--2018-03-07T09-28-10.445Z--0xASDFMEWIFREVNERIGVNERTIGRTNBRT";

        mvc.perform(
            get("/api/wallets/accounts/0xASDFMEWIFREVNERIGVNERTIGRTNBRT/download"))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Type", "application/json"))
            .andExpect(header().string("Content-Disposition", String.format("attachment; filename=%s;", expectedFilename)))
            .andExpect(content().string(walletAccount.getSecretStorageJson()));
    }
}
