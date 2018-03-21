package hut34.wallet.service;

import com.google.common.io.Resources;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.model.WalletAccountType;
import hut34.wallet.testinfra.BaseTest;
import hut34.wallet.testinfra.TestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.web3j.crypto.Credentials;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ManagedAccountServiceTest extends BaseTest {

    @Mock
    private SecretStorage secretStorage;

    @InjectMocks
    private ManagedAccountService managedAccountService;

    @Test
    public void loadCredentials_willDecryptWallet() throws Exception {
        WalletAccount walletAccount = TestData.walletAccount("0xADDRESS");
        walletAccount.setType(WalletAccountType.MANAGED);
        walletAccount.setSecretStorageJson(loadFile("managed-wallet.json"));
        when(secretStorage.loadOrSetPassword("0xADDRESS")).thenReturn("password");

        Credentials credentials = managedAccountService.loadCredentials(walletAccount);

        assertThat(credentials, notNullValue());
        assertThat(credentials.getAddress(), is("0x34e6886b372dcb60a74808738ad7548f61bd84bc"));
        assertThat(credentials.getEcKeyPair().getPrivateKey().toString(),
            is("30691380159174278686825619550221715406229190521042964749892986032278472983892"));
    }

    private String loadFile(String filename) throws IOException {
        return Resources.toString(Resources.getResource(filename), StandardCharsets.UTF_8);
    }
}
