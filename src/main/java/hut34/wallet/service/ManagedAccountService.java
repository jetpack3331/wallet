package hut34.wallet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.util.Assert;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import java.io.IOException;

import static hut34.wallet.model.WalletAccountType.MANAGED;

@Service
public class ManagedAccountService {

    private final SecretStorage secretStorage;

    public ManagedAccountService(SecretStorage secretStorage) {
        this.secretStorage = secretStorage;
    }

    public Credentials loadCredentials(WalletAccount walletAccount) {
        assertManagedAccount(walletAccount);
        try {
            String password = secretStorage.loadOrSetPassword();
            WalletFile walletFile = getWalletFile(walletAccount.getSecretStorageJson());
            return Credentials.create(Wallet.decrypt(password, walletFile));
        } catch (CipherException e) {
            throw new RuntimeException("Error loading managed address credentials", e);
        }
    }

    private WalletFile getWalletFile(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, WalletFile.class);
        } catch (IOException e) {
            throw new RuntimeException("Error serializing wallet file", e);
        }
    }

    private void assertManagedAccount(WalletAccount walletAccount) {
        Assert.notNull(walletAccount, "Account cannot be null");
        Assert.isTrue(walletAccount.getType() == MANAGED, "Invalid account type %s", walletAccount.getType());
    }
}
