package hut34.wallet.framework.kms.config;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.cloudkms.v1.CloudKMSScopes;
import com.google.common.io.Resources;
import hut34.wallet.framework.kms.client.GoogleCloudKmsClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.contrib.gae.datastore.config.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@ConditionalOnProperty(name = "kms.client.enabled", havingValue = "true")
@EnableConfigurationProperties(KmsClientProperties.class)
public class GoogleCloudKmsAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleCloudKmsClient.class);

    private KmsClientProperties properties;

    public GoogleCloudKmsAutoConfiguration(KmsClientProperties properties) {
        this.properties = properties;
    }

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

    @Bean
    @ConditionalOnMissingBean
    public GoogleCredential cloudKmsCredential(HttpTransport httpTransport, JsonFactory jsonFactory) {

        if (StringUtils.isBlank(properties.getCredential())) {
            try {
                LOG.info("Using application default credential");
                return GoogleCredential
                    .getApplicationDefault(httpTransport, jsonFactory)
                    .createScoped(CloudKMSScopes.all());
            } catch (IOException e) {
                throw new ConfigurationException(e, "Cloud KMS credential configuration failed: %s", e.getMessage());
            }
        } else {
            LOG.info("Loading service account credential from file {}", properties.getCredential());
            try (InputStream jsonCredentials = Resources.getResource(properties.getCredential()).openStream()) {
                return GoogleCredential
                    .fromStream(jsonCredentials, httpTransport, jsonFactory)
                    .createScoped(CloudKMSScopes.all());
            } catch (IOException e) {
                throw new ConfigurationException(e,
                    "Cloud KMS credential configuration failed. Ensure you have a credentials file created in src/main/resources/%s." +
                        "See https://developers.google.com/identity/protocols/application-default-credentials.",
                    properties.getCredential());
            }
        }
    }

    @Bean
    public GoogleCloudKmsClient googleCloudKmsClient(GoogleCredential cloudKmsCredential, @Value("app.name") String applicationName) {
        return new GoogleCloudKmsClient(cloudKmsCredential, applicationName);
    }
}
