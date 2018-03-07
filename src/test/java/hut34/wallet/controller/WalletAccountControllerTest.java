package hut34.wallet.controller;

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
