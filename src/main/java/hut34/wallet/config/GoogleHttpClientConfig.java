package hut34.wallet.config;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleHttpClientConfig {

    @Bean
    @ConditionalOnMissingBean
    public HttpTransport httpTransport() {
        return UrlFetchTransport.getDefaultInstance();
    }

    @Bean
    @ConditionalOnMissingBean
    public JsonFactory jsonFactory() {
        return JacksonFactory.getDefaultInstance();
    }
}
