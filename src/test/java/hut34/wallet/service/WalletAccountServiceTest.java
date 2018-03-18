package hut34.wallet.service;

import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.framework.usermanagement.model.UserAdapterGae;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.model.WalletAccountType;
import hut34.wallet.repository.WalletAccountRepository;
import hut34.wallet.testinfra.BaseTest;
import hut34.wallet.testinfra.TestData;
import hut34.wallet.testinfra.rules.SecurityContextRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.contrib.gae.objectify.Refs;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static hut34.wallet.testinfra.MockHelpers.mockSave;
import static hut34.wallet.testinfra.TestData.user;
import static hut34.wallet.testinfra.matcher.Matchers.hasFieldWithUserRef;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WalletAccountServiceTest extends BaseTest {

    @Rule
    public SecurityContextRule securityContextRule = new SecurityContextRule();

    @InjectMocks
    private WalletAccountService walletAccountService;

    @Mock
    private WalletAccountRepository walletAccountRepository;
    @Mock
    private UserAdapterGae userAdapter;
    @Mock
    private SecretStorage secretStorage;

    private User user;

    @Before
    public void before() {
        user = user();
        mockSave(walletAccountRepository, WalletAccount.class);
    }

    @Test
    public void createPrivate_willCreatePrivateAddress() {
        String address = "address";
        when(walletAccountRepository.findById(address)).thenReturn(Optional.empty());
        when(userAdapter.getCurrentUserRequired()).thenReturn(user);

        WalletAccount result = walletAccountService.createPrivate(address, "encryptedPrivateKey");

        assertThat(result.getAddress(), is(address));
        assertThat(result.getSecretStorageJson(), is("encryptedPrivateKey"));
        assertThat(result, hasFieldWithUserRef("owner", user));

        verify(walletAccountRepository).save(result);
    }

    @Test
    public void createPrivate_willError_whenWalletAlreadyExists() {
        String address = "address";
        when(walletAccountRepository.findById(address)).thenReturn(Optional.of(TestData.walletAccount(address)));

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Wallet account already exists for address");
        walletAccountService.createPrivate(address, "encryptedPrivateKey");
    }

    // This test is pretty slow due to the wallet creation/encryption
    @Test
    public void createManaged_willCreateManagedAddress() {
        when(secretStorage.loadOrSetPassword()).thenReturn("password");
        when(walletAccountRepository.findById(anyString())).thenReturn(Optional.empty());
        when(userAdapter.getCurrentUserRequired()).thenReturn(user);

        WalletAccount result = walletAccountService.createManaged();

        assertThat(result.getType(), is(WalletAccountType.MANAGED));
        assertThat(result.getAddress(), notNullValue());
        assertThat(result.getSecretStorageJson(), notNullValue());
        assertThat(result, hasFieldWithUserRef("owner", user));

        verify(walletAccountRepository).save(result);
    }

    @Test
    public void listForCurrentUser() {
        List<WalletAccount> expectedResults = Collections.singletonList(TestData.walletAccount());
        when(walletAccountRepository.findAllByField(WalletAccount.Fields.owner, Refs.key(securityContextRule.getUser()))).thenReturn(expectedResults);

        List<WalletAccount> result = walletAccountService.listForCurrentUser();

        assertThat(result, is(expectedResults));
    }

    @Test
    public void listForCurrentUser_willReturnEmptyList_whenNoCurrentUser() {
        SecurityContextHolder.clearContext();

        List<WalletAccount> result = walletAccountService.listForCurrentUser();

        assertThat(result, empty());
        verifyNoMoreInteractions(walletAccountRepository);
    }

    @Test
    public void get_willReturnEmpty_whenNotFound() {
        String address = "address";
        when(walletAccountRepository.findById(address)).thenReturn(Optional.empty());

        Optional<WalletAccount> result = walletAccountService.get("address");
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void get_willReturnEmpty_whenNoCurrentUser() {
        String address = "address";
        when(walletAccountRepository.findById(address)).thenReturn(Optional.of(TestData.walletAccount(address)));
        SecurityContextHolder.clearContext();

        Optional<WalletAccount> result = walletAccountService.get("address");
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void get_willReturnEmpty_whenCurrentUserNotAuthorised() {
        String address = "address";
        WalletAccount wallet = TestData.walletAccount(address, user("notauth@email.com"));
        when(walletAccountRepository.findById(address)).thenReturn(Optional.of(wallet));

        Optional<WalletAccount> result = walletAccountService.get("address");
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void get_willReturnWallet_whenExistsAndUserAuthorised() {
        String address = "address";
        WalletAccount wallet = TestData.walletAccount(address, securityContextRule.getUser());
        when(walletAccountRepository.findById(address)).thenReturn(Optional.of(wallet));

        Optional<WalletAccount> result = walletAccountService.get("address");
        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getAddress(), is("address"));
    }
}
