package hut34.wallet.framework.usermanagement.controller;

import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.framework.usermanagement.model.UserAdapterGae;
import hut34.wallet.framework.usermanagement.service.UserService;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.OffsetDateTime;
import java.util.Optional;

import static hut34.wallet.util.RestUtils.response;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserAdapterGae gaeUserAdapter;
    private final UserService userService;

    public UserController(UserAdapterGae gaeUserAdapter, UserService userService) {
        this.gaeUserAdapter = gaeUserAdapter;
        this.userService = userService;
    }

    @RequestMapping(method = GET, path = "/me")
    public User user(HttpServletResponse response) {
        Optional<User> currentUser = gaeUserAdapter.getCurrentUser();

        if (currentUser.isPresent()) {
            LOG.debug("Found existing authenticated user {}", currentUser.get().getEmail());
            return response(currentUser);
        }

        LOG.debug("User not authenticated");
        response.setStatus(HttpStatus.SC_NO_CONTENT);
        return null;
    }

    @PostMapping("/terms")
    public User acceptTerms() {
        User currentUser = gaeUserAdapter.getCurrentUserRequired();
        currentUser.setTermsAccepted(OffsetDateTime.now());
        userService.save(currentUser);
        return currentUser;
    }
}
