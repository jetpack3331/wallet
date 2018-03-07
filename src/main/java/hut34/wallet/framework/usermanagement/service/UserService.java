package hut34.wallet.framework.usermanagement.service;

import hut34.wallet.framework.usermanagement.dto.OAuthUserRequest;
import hut34.wallet.framework.usermanagement.dto.UpdateUserRequest;
import hut34.wallet.framework.usermanagement.model.LoginIdentifier;
import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.framework.usermanagement.repository.LoginIdentifierRepository;
import hut34.wallet.framework.usermanagement.repository.UserRepository;
import hut34.wallet.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LoginIdentifierRepository loginIdentifierRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, LoginIdentifierRepository loginIdentifierRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.loginIdentifierRepository = loginIdentifierRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public User update(final String userId, final UpdateUserRequest request) {
        return ofy().transact(() -> {
            User user = userRepository.getById(userId);
            String normalisedEmail = request.getEmail().toLowerCase();
            if (!StringUtils.equals(user.getEmail(), normalisedEmail)) {
                checkAvailability(normalisedEmail);
                loginIdentifierRepository.delete(user.getEmail());
                user.setEmail(normalisedEmail);
                loginIdentifierRepository.save(new LoginIdentifier(user));
            }

            user.setRoles(request.getRoles());
            user.setName(request.getName());

            return userRepository.save(user);
        });
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User create(UpdateUserRequest request, String password) {
        User user = User.byEmail(request.getEmail(), passwordEncoder.encode(password))
            .setName(request.getName())
            .setRoles(request.getRoles());

        return create(user);
    }

    public User create(OAuthUserRequest request) {
        User user = User.byProviderEmail(request.getProvider(), request.getExternalId(), request.getEmail())
            .setName(request.getName())
            .setRoles(request.getRoles());

        return create(user);
    }

    private User create(User user) {
        return ofy().transact(() -> {
            String normalisedEmail = user.getEmail().toLowerCase();
            checkAvailability(normalisedEmail);
            loginIdentifierRepository.save(new LoginIdentifier(user));
            return userRepository.save(user);
        });
    }

    public Optional<User> get(String email) {
        return loginIdentifierRepository.findById(email)
            .map(LoginIdentifier::getUser);
    }

    public Optional<User> getById(String userId) {
        return userRepository.findById(userId);
    }

    private void checkAvailability(String loginIdentifier) {
        Assert.notNull(loginIdentifier, "Availability check failed. Login identifier is required");

        if (loginIdentifierRepository.findById(loginIdentifier.toLowerCase()).isPresent()) {
            throw new LoginIdentifierUnavailableException(loginIdentifier);
        }
    }


}
