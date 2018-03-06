package hut34.wallet.service;

import hut34.wallet.framework.usermanagement.model.UserAdapterGae;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.repository.WalletAccountRepository;
import hut34.wallet.util.Assert;
import org.springframework.stereotype.Service;

@Service
public class WalletAccountService {
    private final WalletAccountRepository walletAccountRepository;
    private final UserAdapterGae userAdapter;

    public WalletAccountService(WalletAccountRepository walletAccountRepository, UserAdapterGae userAdapter) {
        this.walletAccountRepository = walletAccountRepository;
        this.userAdapter = userAdapter;
    }

    @SuppressWarnings("ConstantConditions")
    public WalletAccount create(String address, String encryptedPrivateKey) {
        Assert.isAbsent(walletAccountRepository.findById(address), "Wallet account already exists for address");

        WalletAccount walletAccount = new WalletAccount(address, userAdapter.getCurrentUserRequired())
            .setEncryptedPrivateKey(encryptedPrivateKey);

        return walletAccountRepository.save(walletAccount);
    }

}
