package hut34.wallet.service;

import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.framework.usermanagement.model.UserAdapterGae;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.repository.WalletAccountRepository;
import hut34.wallet.testinfra.TestData;
import hut34.wallet.testinfra.rules.LocalServicesRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static hut34.wallet.testinfra.MockHelpers.mockSave;
import static hut34.wallet.testinfra.matcher.Matchers.hasFieldWithUserRef;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WalletAccountServiceTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    @Rule
    public LocalServicesRule localServicesRule = new LocalServicesRule();

    @InjectMocks
    private WalletAccountService walletAccountService;

    @Mock
    private WalletAccountRepository walletAccountRepository;
    @Mock
    private UserAdapterGae userAdapter;

    private User user;

    @Before
    public void before() {
        user = TestData.user();
        mockSave(walletAccountRepository, WalletAccount.class);
    }

    @Test
    public void create() {
        String address = "address";
        when(walletAccountRepository.findById(address)).thenReturn(Optional.empty());
        when(userAdapter.getCurrentUserRequired()).thenReturn(user);

        WalletAccount result = walletAccountService.create(address, "encryptedPrivateKey");

        assertThat(result.getAddress(), is(address));
        assertThat(result.getSecretStorageJson(), is("encryptedPrivateKey"));
        assertThat(result, hasFieldWithUserRef("owner", user));

        verify(walletAccountRepository).save(result);
    }

    @Test
    public void create_willError_whenWalletAlreadyExists() {
        String address = "address";
        when(walletAccountRepository.findById(address)).thenReturn(Optional.of(TestData.walletAccount(address)));

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Wallet account already exists for address");
        walletAccountService.create(address, "encryptedPrivateKey");
    }

}
