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

    private static final String SECRET_FILENAME = "HUT34_SECRET";

    private final OAuth2ClientContext oAuth2ClientContext;
    private final HttpTransport httpTransport;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public GoogleDriveSecretStorage(OAuth2ClientContext oAuth2ClientContext, HttpTransport httpTransport) {
        this.oAuth2ClientContext = oAuth2ClientContext;
        this.httpTransport = httpTransport;
    }

    public String loadOrSetPassword() {
        GoogleDriveAppData googleDriveAppData = initDriveForCurrentUser();
        Optional<File> optionalFile = googleDriveAppData.findFile(SECRET_FILENAME);
        File secretFile = optionalFile.orElseGet(() -> createPasswordFile(googleDriveAppData));
        return googleDriveAppData.readFile(secretFile);
    }

    private GoogleDriveAppData initDriveForCurrentUser() {
        return new GoogleDriveAppData(httpTransport, oAuth2ClientContext.getAccessToken().getValue());
    }

    private File createPasswordFile(GoogleDriveAppData googleDriveAppData) {
        return googleDriveAppData.createFile(SECRET_FILENAME, randomAlphanumeric(24));
    }
}
