package hut34.wallet.model;

import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.testinfra.TestData;
import hut34.wallet.testinfra.rules.LocalServicesRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static hut34.wallet.model.WalletAccountType.*;
import static hut34.wallet.testinfra.matcher.Matchers.hasFieldWithUserRef;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class WalletAccountTest {

    @Rule
    public LocalServicesRule localServicesRule = new LocalServicesRule();
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor() {
        User owner = TestData.user();
        WalletAccount walletAccount = new WalletAccount(PRIVATE, "123", owner);

        assertThat(walletAccount.getAddress(), is("123"));
        assertThat(walletAccount, hasFieldWithUserRef("owner", owner));
    }

    @Test
    public void constructor_willFail_whenBlankAddress() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("address required");

        new WalletAccount(PRIVATE, "  ", User.byEmail("email", "password"));
    }

    @Test
    public void constructor_willFail_whenNullOwner() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("owner required");

        new WalletAccount(PRIVATE, "123", null);
    }

}
