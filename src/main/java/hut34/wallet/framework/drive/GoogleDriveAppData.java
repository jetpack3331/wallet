package hut34.wallet.framework.drive;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;

public class GoogleDriveAppData {
    private static final Logger LOG = LoggerFactory.getLogger(GoogleDriveAppData.class);
    private static final String APP_DATA_FOLDER = "appDataFolder";
    private Drive drive;

    public GoogleDriveAppData(HttpTransport httpTransport, String accessToken) {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        drive = new Drive.Builder(httpTransport, JacksonFactory.getDefaultInstance(), credential)
            .setApplicationName("hut34-wallet")
            .build();
    }

    public File createFile(String filename, String contents) {
        try {
            File fileMetadata = new File();
            fileMetadata.setName(filename);
            fileMetadata.setParents(Collections.singletonList(APP_DATA_FOLDER));
            ByteArrayContent mediaContent = new ByteArrayContent("text/plain", contents.getBytes(StandardCharsets.UTF_8));
            LOG.info("Creating drive appData file {}", filename);
            return drive.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        } catch (IOException e) {
            throw new RuntimeException("Error creating file in drive", e);
        }
    }

    public Optional<File> findFile(String filename) {
        try {
            LOG.info("Looking for drive appData file with name {}", filename);
            FileList files = drive.files().list()
                .setSpaces(APP_DATA_FOLDER)
                .execute();

            return files.getFiles().stream()
                .filter(file -> file.getName().equals(filename))
                .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Error finding file in drive", e);
        }
    }

    public String readFile(File file) {
        try {
            LOG.info("Reading drive appData file {}", file.getId());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            drive.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
            return outputStream.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new RuntimeException("Error creating file in drive", e);
        }
    }
}
