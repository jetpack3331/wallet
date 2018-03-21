package hut34.wallet.service;

public interface SecretStorage {
    String loadOrSetPassword(String key);
}
