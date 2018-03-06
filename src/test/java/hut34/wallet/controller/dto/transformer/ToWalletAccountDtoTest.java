package hut34.wallet.controller.dto.transformer;

import hut34.wallet.controller.dto.WalletAccountDto;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.testinfra.TestData;
import hut34.wallet.testinfra.rules.LocalServicesRule;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ToWalletAccountDtoTest {

    @Rule
    public LocalServicesRule localServicesRule = new LocalServicesRule();

    @Test
    public void apply() {
        WalletAccount entity = TestData.setCreatedUpdated(new WalletAccount("someAddress", TestData.user()))
            .setSecretStorageJson("nfefe8fehfekfn3f9834fn34ofi4nf43kljnblk4949489484848484");

        WalletAccountDto dto = Transformers.TO_WALLET_ACCOUNT_DTO.apply(entity);

        assertThat(dto.getAddress(), is(entity.getAddress()));
        assertThat(dto.getSecretStorageJson(), is(entity.getSecretStorageJson()));
        assertThat(dto.getCreated(), is(entity.getCreated()));
        assertThat(dto.getUpdated(), is(entity.getUpdated()));
    }

}
