package hut34.wallet.service;

import hut34.wallet.framework.MockUrlFetchTransport;
import hut34.wallet.framework.drive.GoogleDriveAppDataTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GoogleDriveSecretStorageTest {

    private MockUrlFetchTransport httpTransport;
    private GoogleDriveSecretStorage googleDriveSecretStorage;

    @Before
    public void setUp() {
        OAuth2ClientContext oAuth2ClientContext = new DefaultOAuth2ClientContext(new DefaultOAuth2AccessToken("test"));
        httpTransport = new MockUrlFetchTransport();
        googleDriveSecretStorage = new GoogleDriveSecretStorage(oAuth2ClientContext, httpTransport);
    }

    @Test
    public void loadOrSetPassword_willReturnExistingFileIfFound() {
        httpTransport.addResponse(GoogleDriveAppDataTest.fileFoundResponse("HUT34_SECRET"));
        httpTransport.addResponse(GoogleDriveAppDataTest.fileDownloadResponse("TEST_PASSWORD"));

        String password = googleDriveSecretStorage.loadOrSetPassword();

        assertThat(httpTransport.getRequestLog().size(), is(2));
        assertThat(password, is("TEST_PASSWORD"));
    }

    @Test
    public void loadOrSetPassword_willCreateNewFileIfNotFound() {
        httpTransport.addResponse(GoogleDriveAppDataTest.noFilesFoundResponse());
        httpTransport.addResponse(GoogleDriveAppDataTest.resumableUploadResponse());
        httpTransport.addResponse(GoogleDriveAppDataTest.putFileResponse());
        httpTransport.addResponse(GoogleDriveAppDataTest.fileDownloadResponse("TEST_PASSWORD"));

        String password = googleDriveSecretStorage.loadOrSetPassword();

        assertThat(httpTransport.getRequestLog().size(), is(4));
        assertThat(password, is("TEST_PASSWORD"));
    }
}
