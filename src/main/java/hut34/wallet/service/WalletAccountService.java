package hut34.wallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.objectify.Key;
import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.framework.usermanagement.model.UserAdapterGae;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.model.WalletAccountType;
import hut34.wallet.repository.WalletAccountRepository;
import hut34.wallet.util.Assert;
import hut34.wallet.util.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static hut34.wallet.model.WalletAccountType.MANAGED;
import static hut34.wallet.model.WalletAccountType.PRIVATE;

@Service
public class WalletAccountService {

    private static final Logger LOG = LoggerFactory.getLogger(WalletAccountService.class);

    private final WalletAccountRepository walletAccountRepository;
    private final UserAdapterGae userAdapter;
    private final SecretStorage secretStorage;


    public WalletAccountService(WalletAccountRepository walletAccountRepository, UserAdapterGae userAdapter, SecretStorage secretStorage) {
        this.walletAccountRepository = walletAccountRepository;
        this.userAdapter = userAdapter;
        this.secretStorage = secretStorage;
    }

    @SuppressWarnings("ConstantConditions")
    public WalletAccount createPrivate(String address, String secretStorageJson) {
        return create(PRIVATE, address, secretStorageJson);
    }

    public WalletAccount createManaged() {
        LOG.info("Creating new managed wallet account");

        try {
            LOG.debug("Retrieving managed password");
            String password = secretStorage.loadOrSetPassword();

            LOG.debug("Creating and encrypting wallet");
            WalletFile walletFile = Wallet.createStandard(password, Keys.createEcKeyPair());
            String secretJson = getWalletAsJson(walletFile);

            return create(MANAGED, walletFile.getAddress(), secretJson);

        } catch (CipherException | NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Error creating wallet file", e);
        }
    }

    private WalletAccount create(WalletAccountType type, String address, String secretStorageJson) {
        String hexAddress = hexifyAddress(address);
        Assert.isAbsent(walletAccountRepository.findById(hexAddress), "Wallet account already exists for address");

        LOG.info("Creating new {} WalletAccount for address {}", type, hexAddress);
        WalletAccount walletAccount = new WalletAccount(type, hexAddress, userAdapter.getCurrentUserRequired())
            .setSecretStorageJson(secretStorageJson);

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

    public WalletAccount getOrThrow(String address) {
        Optional<WalletAccount> optional = get(address);
        return optional.orElseThrow(NotFoundException::new);
    }

    private boolean currentUserAuthorisedToView(Key<User> objectOwner) {
        Optional<Key<User>> currentUserKey = UserAdapterGae.currentUserKey();
        return currentUserKey.isPresent() && currentUserKey.get().equals(objectOwner);
    }

    private String hexifyAddress(String address) {
        return StringUtils.prependIfMissingIgnoreCase(address, "0x", "0x", "0X");
    }

    private String getWalletAsJson(WalletFile walletFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(walletFile);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing wallet file", e);
        }
    }
}
