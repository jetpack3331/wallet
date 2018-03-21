package hut34.wallet.framework.kms.client;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.cloudkms.v1.CloudKMS;
import com.google.api.services.cloudkms.v1.model.DecryptRequest;
import com.google.api.services.cloudkms.v1.model.DecryptResponse;
import com.google.api.services.cloudkms.v1.model.EncryptRequest;
import com.google.api.services.cloudkms.v1.model.EncryptResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GoogleCloudKmsClient {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleCloudKmsClient.class);

    private final CloudKMS cloudKMS;

    public GoogleCloudKmsClient(GoogleCredential cloudKmsCredential, String applicationName) {
        cloudKMS = new CloudKMS.Builder(cloudKmsCredential.getTransport(), cloudKmsCredential.getJsonFactory(), cloudKmsCredential)
            .setApplicationName(applicationName)
            .build();
    }

    /**
     * Encrypt some text with a KMS Key
     *
     * @param cryptoKeyName key resource in format projects/%s/locations/%s/keyRings/%s/cryptoKeys/%s
     * @param plaintext     text to encrypt
     * @return base64 encoded ciphertext
     * @throws IOException if error occurs during encryption operation
     */
    public String encrypt(String cryptoKeyName, String plaintext) throws IOException {

        EncryptRequest request = new EncryptRequest().encodePlaintext(plaintext.getBytes(StandardCharsets.UTF_8));

        LOG.info("Starting KMS encryption");
        EncryptResponse response = cloudKMS.projects().locations().keyRings().cryptoKeys()
            .encrypt(cryptoKeyName, request)
            .execute();

        LOG.info("KMS encryption complete");
        return response.getCiphertext();
    }

    /**
     * Decrypt some text with a KMS Key
     *
     * @param cryptoKeyName key resource in format projects/%s/locations/%s/keyRings/%s/cryptoKeys/%s
     * @param ciphertext    base64 encoded ciphertext to decrypt
     * @return decrypted plaintext
     * @throws IOException if error occurs during decryption operation
     */
    public String decrypt(String cryptoKeyName, String ciphertext) throws IOException {

        DecryptRequest request = new DecryptRequest().setCiphertext(ciphertext);

        LOG.info("Starting KMS decryption");
        DecryptResponse response = cloudKMS.projects().locations().keyRings().cryptoKeys()
            .decrypt(cryptoKeyName, request)
            .execute();

        LOG.info("KMS decryption complete");
        return new String(response.decodePlaintext(), StandardCharsets.UTF_8);
    }
}
