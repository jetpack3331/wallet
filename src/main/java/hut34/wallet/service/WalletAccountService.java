package hut34.wallet.service;

import com.googlecode.objectify.Key;
import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.framework.usermanagement.model.UserAdapterGae;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.repository.WalletAccountRepository;
import hut34.wallet.util.Assert;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            .setSecretStorageJson(encryptedPrivateKey);

        return walletAccountRepository.save(walletAccount);
    }

    public List<WalletAccount> listForCurrentUser() {
        return UserAdapterGae.currentUserKey()
            .map(key -> walletAccountRepository.findAllByField(WalletAccount.Fields.owner, key))
            .orElseGet(ArrayList::new);
    }

    public Optional<WalletAccount> get(String address) {
        return walletAccountRepository.findById(address)
            .filter(walletAccount -> currentUserAuthorisedToView(walletAccount.getOwnerKey()));
    }

    private boolean currentUserAuthorisedToView(Key<User> objectOwner) {
        Optional<Key<User>> currentUserKey = UserAdapterGae.currentUserKey();
        return currentUserKey.isPresent() && currentUserKey.get().equals(objectOwner);
    }
}
