package hut34.wallet.framework.kms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Google Cloud KMS Client configuration properties.
 */
@ConfigurationProperties("kms.client")
public class KmsClientProperties {

    private boolean enabled;
    private String credential;

    public boolean isEnabled() {
        return enabled;
    }

    public KmsClientProperties setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getCredential() {
        return credential;
    }

    public KmsClientProperties setCredential(String credential) {
        this.credential = credential;
        return this;
    }
}
