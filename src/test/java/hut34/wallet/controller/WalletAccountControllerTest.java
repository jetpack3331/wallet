package hut34.wallet.controller;

import hut34.wallet.controller.dto.CreateWalletRequest;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.service.WalletAccountService;
import hut34.wallet.testinfra.BaseControllerTest;
import hut34.wallet.testinfra.TestData;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
        CreateWalletRequest request = new CreateWalletRequest("0xASDFMEWIFREVNERIGVNERTIGRTNBRT", "femfefiefowejfno43ifnm34ofin3498f3498598");
        WalletAccount walletAccount = new WalletAccount(request.getAddress(), TestData.user())
            .setEncryptedPrivateKey(request.getEncryptedPrivateKey());
        when(walletAccountService.create(request.getAddress(), request.getEncryptedPrivateKey())).thenReturn(walletAccount);

        mvc.perform(
            post("/api/wallets/accounts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("address", is(request.getAddress())))
            .andExpect(jsonPath("encryptedPrivateKey", is(request.getEncryptedPrivateKey())));
    }


}
