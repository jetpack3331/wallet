package hut34.wallet.testinfra;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.testing.auth.oauth2.MockGoogleCredential;
import com.google.api.client.http.HttpTransport;
import hut34.wallet.Application;
import hut34.wallet.framework.MockUrlFetchTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(Application.class)
public class TestApplicationContext {

    @Bean
    public HttpTransport httpTransport() {
        return new MockUrlFetchTransport();
    }

    @Bean
    public GoogleCredential cloudKmsCredential(HttpTransport httpTransport) {
        return new MockGoogleCredential.Builder()
            .setTransport(httpTransport)
            .build();
    }
}
