package hut34.wallet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.model.WalletAccountType;
import hut34.wallet.util.Assert;
import hut34.wallet.util.NotFoundException;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ManagedAccountService {

    private final WalletAccountService walletAccountService;
    private final SecretStorage secretStorage;

    public ManagedAccountService(WalletAccountService walletAccountService, SecretStorage secretStorage) {
        this.walletAccountService = walletAccountService;
        this.secretStorage = secretStorage;
    }

    public Credentials loadCredentials(String address) {
        Optional<WalletAccount> optionalAccount = walletAccountService.get(address);
        WalletAccount account = optionalAccount.orElseThrow(NotFoundException::new);
        Assert.isTrue(account.getType() == WalletAccountType.MANAGED, "Invalid account type %s", account.getType());

        try {
            String password = secretStorage.loadOrSetPassword();
            WalletFile walletFile = getWalletFile(account.getSecretStorageJson());
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
}
