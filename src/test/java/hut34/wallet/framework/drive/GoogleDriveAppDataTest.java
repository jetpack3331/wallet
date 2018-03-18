package hut34.wallet.framework.drive;

import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.services.drive.model.File;
import hut34.wallet.framework.MockUrlFetchTransport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GoogleDriveAppDataTest {

    private MockUrlFetchTransport httpTransport;
    private GoogleDriveAppData googleDriveAppData;

    @Before
    public void setup() {
        httpTransport = new MockUrlFetchTransport();
        googleDriveAppData = new GoogleDriveAppData(httpTransport, "test");
    }

    @Test
    public void createFile() {
        httpTransport.addResponse(resumableUploadResponse());
        httpTransport.addResponse(putFileResponse());

        File result = googleDriveAppData.createFile("testFile", "abcd1234");

        assertThat(result.getId(), is("FILE_ID"));
        assertThat(httpTransport.getRequestLog().size(), is(2));
        assertThat(httpTransport.getLastRequest().getUrl(), is("https://uploadurl"));
    }

    @Test
    public void findFile_willFindExistingFile() {
        httpTransport.addResponse(fileFoundResponse("HUT34_SECRET"));

        Optional<File> optionalFile = googleDriveAppData.findFile("HUT34_SECRET");

        File result = optionalFile.orElseThrow(RuntimeException::new);
        assertThat(result.getId(), is("FILE_ID"));
        assertThat(result.getName(), is("HUT34_SECRET"));
    }

    @Test
    public void findFile_willReturnEmptyForMissingFile() {
        httpTransport.addResponse(noFilesFoundResponse());

        Optional<File> optionalFile = googleDriveAppData.findFile("HUT34_SECRET");

        assertThat(optionalFile.isPresent(), is(false));
    }

    @Test
    public void findFile_willMatchFileOnCorrectName() {
        httpTransport.addResponse(fileFoundResponse("HUT34_OTHER"));

        Optional<File> optionalFile = googleDriveAppData.findFile("HUT34_SECRET");

        assertThat(optionalFile.isPresent(), is(false));
    }

    @Test
    public void readFile_willReturnFileContent() {
        httpTransport.addResponse(fileDownloadResponse("FILE_CONTENT"));

        String contents = googleDriveAppData.readFile(new File().setId("FILE_ID"));

        assertThat(contents, is("FILE_CONTENT"));
    }

    public static MockLowLevelHttpResponse resumableUploadResponse() {
        return new MockLowLevelHttpResponse().addHeader("Location", "https://uploadurl");
    }

    public static MockLowLevelHttpResponse putFileResponse() {
        return new MockLowLevelHttpResponse().setContent("{\"id\": \"FILE_ID\"}");
    }

    public static MockLowLevelHttpResponse noFilesFoundResponse() {
        return new MockLowLevelHttpResponse().setContent(
            "{\n" +
                " \"kind\": \"drive#fileList\",\n" +
                " \"incompleteSearch\": false,\n" +
                " \"files\": []\n" +
                "}");
    }

    public static MockLowLevelHttpResponse fileFoundResponse(String name) {
        return new MockLowLevelHttpResponse().setContent(
            "{\n" +
                " \"kind\": \"drive#fileList\",\n" +
                " \"incompleteSearch\": false,\n" +
                " \"files\": [" +
                "  {\n" +
                "   \"kind\": \"drive#file\",\n" +
                "   \"id\": \"FILE_ID\",\n" +
                "   \"name\": \"" + name + "\",\n" +
                "   \"mimeType\": \"text/plain\"\n" +
                "  }" +
                "]}");
    }

    public static MockLowLevelHttpResponse fileDownloadResponse(String content) {
        return new MockLowLevelHttpResponse().setContent(content);
    }
}
