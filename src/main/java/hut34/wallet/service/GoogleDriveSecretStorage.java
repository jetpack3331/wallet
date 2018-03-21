package hut34.wallet.service;

import com.google.api.client.http.HttpTransport;
import com.google.api.services.drive.model.File;
import hut34.wallet.framework.drive.GoogleDriveAppData;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Service
public class GoogleDriveSecretStorage implements SecretStorage {

    private static final String FILENAME_PREFIX = "hut34-v1";

    private final OAuth2ClientContext oAuth2ClientContext;
    private final HttpTransport httpTransport;
    private final SecretEncryption secretEncryption;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public GoogleDriveSecretStorage(OAuth2ClientContext oAuth2ClientContext, HttpTransport httpTransport, SecretEncryption secretEncryption) {
        this.oAuth2ClientContext = oAuth2ClientContext;
        this.httpTransport = httpTransport;
        this.secretEncryption = secretEncryption;
    }

    public String loadOrSetPassword(String address) {
        GoogleDriveAppData googleDriveAppData = initDriveForCurrentUser();
        String passwordFile = getPasswordFileName(address);

        Optional<File> optionalFile = googleDriveAppData.findFile(passwordFile);
        File secretFile = optionalFile.orElseGet(() -> createPasswordFile(googleDriveAppData, passwordFile));

        String encryptedPassword = googleDriveAppData.readFile(secretFile);
        return secretEncryption.decrypt(encryptedPassword);
    }

    private GoogleDriveAppData initDriveForCurrentUser() {
        return new GoogleDriveAppData(httpTransport, oAuth2ClientContext.getAccessToken().getValue());
    }

    private String getPasswordFileName(String address) {
        return String.format("%s-%s", FILENAME_PREFIX, address);
    }

    private File createPasswordFile(GoogleDriveAppData googleDriveAppData, String filename) {
        String encryptedPassword = secretEncryption.encrypt(randomAlphanumeric(24));
        return googleDriveAppData.createFile(filename, encryptedPassword);
    }
}
