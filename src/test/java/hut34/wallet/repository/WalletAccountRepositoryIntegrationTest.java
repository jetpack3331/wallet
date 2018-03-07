package hut34.wallet.repository;

import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.testinfra.TestData;
import hut34.wallet.testinfra.BaseIntegrationTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class WalletAccountRepositoryIntegrationTest extends BaseIntegrationTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Autowired
    private WalletAccountRepository repository;

    private User user;

    @Before
    public void before() {
        user = userRepository.save(TestData.user("admin@3wks.com.au"));
    }

    @Test
    @WithMockUser("admin@3wks.com.au")
    public void save_get() {
        WalletAccount walletAccount = new WalletAccount("234", user)
            .setSecretStorageJson("someencryptedprivateKey");

        repository.save(walletAccount);
        WalletAccount retrieved = repository.getById(walletAccount.getAddress());

        assertThat(retrieved.getAddress(), is(walletAccount.getAddress()));
        assertThat(retrieved.getOwner(), is(user));
        assertThat(retrieved.getSecretStorageJson(), is(walletAccount.getSecretStorageJson()));
    }

}
