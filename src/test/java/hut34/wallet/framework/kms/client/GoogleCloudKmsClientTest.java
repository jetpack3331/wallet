package hut34.wallet.framework.kms.client;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.testing.auth.oauth2.MockGoogleCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.client.util.Base64;
import hut34.wallet.framework.MockUrlFetchTransport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GoogleCloudKmsClientTest {

    private MockUrlFetchTransport httpTransport;
    private GoogleCloudKmsClient kmsClient;
    private String keyName = "projects/my-project/locations/global/keyRings/my-key-ring/cryptoKeys/my-crypto-key";

    @Before
    public void setup() {
        httpTransport = new MockUrlFetchTransport();

        GoogleCredential cloudKmsCredential = new MockGoogleCredential.Builder()
            .setTransport(httpTransport)
            .setJsonFactory(JacksonFactory.getDefaultInstance())
            .build();

        kmsClient = new GoogleCloudKmsClient(cloudKmsCredential, "HUT34 Wallet Test");
    }

    @Test
    public void encrypt() throws IOException {
        httpTransport.addResponse(encryptResponse());

        String result = kmsClient.encrypt(keyName, "PLAINTEXT");

        assertThat(result, is("CIPHERTEXT"));
        assertThat(httpTransport.getRequestLog().size(), is(1));
        assertThat(httpTransport.getLastRequest().getUrl(), is(
            "https://cloudkms.googleapis.com/v1/projects/my-project/locations/global/keyRings/my-key-ring/cryptoKeys/my-crypto-key:encrypt"));
    }

    @Test
    public void decrypt() throws IOException {
        httpTransport.addResponse(decryptResponse());

        String result = kmsClient.decrypt(keyName, "CIPHERTEXT");

        assertThat(result, is("PLAINTEXT"));
        assertThat(httpTransport.getRequestLog().size(), is(1));
        assertThat(httpTransport.getLastRequest().getUrl(), is(
            "https://cloudkms.googleapis.com/v1/projects/my-project/locations/global/keyRings/my-key-ring/cryptoKeys/my-crypto-key:decrypt"));

    }

    public static MockLowLevelHttpResponse encryptResponse() {
        return new MockLowLevelHttpResponse().setContent("{\"name\": \"key-name\", \"ciphertext\": \"CIPHERTEXT\"}");
    }

    public static MockLowLevelHttpResponse decryptResponse() {
        String base64Plaintext = Base64.encodeBase64String("PLAINTEXT".getBytes(StandardCharsets.UTF_8));
        return new MockLowLevelHttpResponse().setContent("{\"plaintext\": \"" + base64Plaintext + "\"}");
    }
}
