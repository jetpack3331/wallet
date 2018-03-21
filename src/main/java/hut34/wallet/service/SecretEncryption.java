package hut34.wallet.service;

/**
 * Service for encrypting/decrypting secrets.
 */
public interface SecretEncryption {

    String encrypt(String plaintext);

    String decrypt(String ciphertext);
}
