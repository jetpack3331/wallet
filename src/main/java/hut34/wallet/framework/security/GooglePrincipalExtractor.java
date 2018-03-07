package hut34.wallet.framework.security;

import hut34.wallet.framework.usermanagement.dto.OAuthUserRequest;
import hut34.wallet.framework.usermanagement.model.AuthProvider;
import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.framework.usermanagement.service.UserService;
import hut34.wallet.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class GooglePrincipalExtractor implements PrincipalExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(GooglePrincipalExtractor.class);

    private final UserService userService;
    private final UserDetailsManager userDetailsManager;

    public GooglePrincipalExtractor(UserService userService, UserDetailsManager userDetailsManager) {
        this.userService = userService;
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        String externalId = (String) map.get("sub");
        String email = (String) map.get("email");
        Assert.notNull(email, "External ID must be provided in details map");
        Assert.notNull(email, "Email must be provided in details map");

        Optional<User> optionalUser = this.userService.get(email);
        if (!optionalUser.isPresent()) {
            LOG.info("Auto registering user with email '{}'", email);
            String name = (String) map.get("name");
            OAuthUserRequest userRequest = new OAuthUserRequest();
            userRequest.setProvider(AuthProvider.GOOGLE);
            userRequest.setExternalId(externalId);
            userRequest.setEmail(email);
            userRequest.setName(name);

            userService.create(userRequest);
        }

        LOG.debug("Loading user details for email '{}'", email);
        return userDetailsManager.loadUserByUsername(email);
    }
}
