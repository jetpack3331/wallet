package hut34.wallet.service;

import hut34.wallet.framework.MockUrlFetchTransport;
import hut34.wallet.framework.drive.GoogleDriveAppDataTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GoogleDriveSecretStorageTest {

    private MockUrlFetchTransport httpTransport;
    private GoogleDriveSecretStorage googleDriveSecretStorage;
    @Mock
    private SecretEncryption secretEncryption;

    @Before
    public void setUp() {
        OAuth2ClientContext oAuth2ClientContext = new DefaultOAuth2ClientContext(new DefaultOAuth2AccessToken("test"));
        httpTransport = new MockUrlFetchTransport();
        googleDriveSecretStorage = new GoogleDriveSecretStorage(oAuth2ClientContext, httpTransport, secretEncryption);
    }

    @Test
    public void loadOrSetPassword_willReturnExistingFileIfFound() {
        httpTransport.addResponse(GoogleDriveAppDataTest.fileFoundResponse("hut34-v1-0xADDRESS"));
        httpTransport.addResponse(GoogleDriveAppDataTest.fileDownloadResponse("CIPHER_PASSWORD"));
        when(secretEncryption.decrypt("CIPHER_PASSWORD")).thenReturn("PLAIN_PASSWORD");

        String password = googleDriveSecretStorage.loadOrSetPassword("0xADDRESS");

        assertThat(httpTransport.getRequestLog().size(), is(2));
        assertThat(password, is("PLAIN_PASSWORD"));
    }

    @Test
    public void loadOrSetPassword_willCreateNewFileIfNotFound() {
        httpTransport.addResponse(GoogleDriveAppDataTest.noFilesFoundResponse());
        httpTransport.addResponse(GoogleDriveAppDataTest.resumableUploadResponse());
        httpTransport.addResponse(GoogleDriveAppDataTest.putFileResponse());
        httpTransport.addResponse(GoogleDriveAppDataTest.fileDownloadResponse("CIPHER_PASSWORD"));
        when(secretEncryption.encrypt(anyString())).thenReturn("CIPHER_PASSWORD");
        when(secretEncryption.decrypt("CIPHER_PASSWORD")).thenReturn("PLAIN_PASSWORD");

        String password = googleDriveSecretStorage.loadOrSetPassword("0xADDRESS");

        assertThat(password, is("PLAIN_PASSWORD"));
        assertThat(httpTransport.getRequestLog().size(), is(4));
    }
}
