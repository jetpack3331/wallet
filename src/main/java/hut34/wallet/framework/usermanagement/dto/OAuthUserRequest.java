package hut34.wallet.framework.usermanagement.dto;

import hut34.wallet.framework.usermanagement.model.AuthProvider;
import org.hibernate.validator.constraints.NotBlank;

public class OAuthUserRequest extends UpdateUserRequest {

    private AuthProvider provider;

    @NotBlank
    private String externalId;

    public AuthProvider getProvider() {
        return provider;
    }

    public OAuthUserRequest setProvider(AuthProvider provider) {
        this.provider = provider;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public OAuthUserRequest setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }
}
