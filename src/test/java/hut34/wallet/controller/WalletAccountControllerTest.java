package hut34.wallet.controller;

import hut34.wallet.controller.dto.CreateWalletRequest;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.service.WalletAccountService;
import hut34.wallet.testinfra.BaseControllerTest;
import hut34.wallet.testinfra.TestData;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WalletAccountControllerTest extends BaseControllerTest {

    @Mock
    private WalletAccountService walletAccountService;

    @Override
    protected Object controller() {
        return new WalletAccountController(walletAccountService);
    }

    @Test
    public void create() throws Exception {
        CreateWalletRequest request = new CreateWalletRequest("0x236cBCDf103a77F6401cF40cAc1F22Ca80B7aaE4", "femfefiefowejfno43ifnm34ofin3498f3498598");
        WalletAccount walletAccount = new WalletAccount(request.getAddress(), TestData.user())
            .setSecretStorageJson(request.getSecretStorageJson());
        when(walletAccountService.create(request.getAddress(), request.getSecretStorageJson())).thenReturn(walletAccount);

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


}
