package hut34.wallet.service;

import com.google.api.client.http.HttpTransport;
import com.google.api.services.drive.model.File;
import hut34.wallet.framework.drive.GoogleDriveAppData;
import hut34.wallet.util.PasswordGenerator;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoogleDriveSecretStorage implements SecretStorage {

    private static final String FILENAME_PREFIX = "hut34-v1";
    private static final int PASSWORD_LENGTH = 24;

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
        String encryptedPassword = secretEncryption.encrypt(PasswordGenerator.alphanumeric(PASSWORD_LENGTH));
        return googleDriveAppData.createFile(filename, encryptedPassword);
    }
}
