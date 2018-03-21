package hut34.wallet.service;

import hut34.wallet.framework.kms.client.GoogleCloudKmsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KmsSecretEncryption implements SecretEncryption {

    private GoogleCloudKmsClient kmsClient;
    private String keyName;

    public KmsSecretEncryption(GoogleCloudKmsClient kmsClient, @Value("${kms.keys.drive}") String keyName) {
        this.kmsClient = kmsClient;
        this.keyName = keyName;
    }

    @Override
    public String encrypt(String plaintext) {
        try {
            return kmsClient.encrypt(keyName, plaintext);
        } catch (IOException e) {
            throw new RuntimeException("Error encrypting provided text", e);
        }
    }

    @Override
    public String decrypt(String ciphertext) {
        try {
            return kmsClient.decrypt(keyName, ciphertext);
        } catch (IOException e) {
            throw new RuntimeException("Error decrypting provided ciphertext", e);
        }
    }
}
