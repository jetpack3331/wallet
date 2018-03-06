package hut34.wallet.framework.security;

import hut34.wallet.framework.usermanagement.dto.UpdateUserRequest;
import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.framework.usermanagement.service.UserService;
import hut34.wallet.util.Assert;
import org.apache.commons.lang3.RandomStringUtils;
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
        String email = (String) map.get("email");
        Assert.notNull(email, "Email must be provided in details map");

        Optional<User> optionalUser = this.userService.get(email);
        if (!optionalUser.isPresent()) {
            LOG.info("Auto registering user with email '{}'", email);
            String name = (String) map.get("name");
            UpdateUserRequest updateUserRequest = new UpdateUserRequest()
                .setEmail(email)
                .setName(name);

            userService.create(updateUserRequest, RandomStringUtils.randomAlphanumeric(24));
        }

        LOG.debug("Loading user details for email '{}'", email);
        return userDetailsManager.loadUserByUsername(email);
    }
}
