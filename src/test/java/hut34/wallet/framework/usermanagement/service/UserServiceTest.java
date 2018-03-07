package hut34.wallet.framework.usermanagement.service;

import com.google.api.client.util.Sets;
import hut34.wallet.framework.usermanagement.Role;
import hut34.wallet.framework.usermanagement.dto.OAuthUserRequest;
import hut34.wallet.framework.usermanagement.dto.UpdateUserRequest;
import hut34.wallet.framework.usermanagement.model.AuthProvider;
import hut34.wallet.framework.usermanagement.model.LoginIdentifier;
import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.framework.usermanagement.repository.LoginIdentifierRepository;
import hut34.wallet.framework.usermanagement.repository.UserRepository;
import hut34.wallet.testinfra.BaseTest;
import hut34.wallet.testinfra.rules.LocalServicesRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static hut34.wallet.testinfra.MockHelpers.mockSave;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest extends BaseTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Rule
    public LocalServicesRule localServicesRule = new LocalServicesRule();

    private static final String EMAIL = "foo@example.org";
    private static final String PASSWORD = "password";

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoginIdentifierRepository loginIdentifierRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @Before
    public void setUp() {
        user = User.byEmail(EMAIL, PASSWORD);
        when(userRepository.getById(user.getId())).thenReturn(user);
        mockSave(userRepository, User.class);
    }

    @Test
    public void create_WillCreateNewUserForOAuthProvider() {
        OAuthUserRequest userRequest = new OAuthUserRequest();
        userRequest.setProvider(AuthProvider.GOOGLE);
        userRequest.setExternalId("googleId");
        userRequest.setEmail("bob@email.com");
        userRequest.setName("Test User");

        when(loginIdentifierRepository.findById(userRequest.getEmail())).thenReturn(Optional.empty());

        User newUser = userService.create(userRequest);

        assertThat(newUser.getEmail(), is("bob@email.com"));
        assertThat(newUser.getExternalId(), is("googleId"));
        assertThat(newUser.getName(), is("Test User"));
        assertThat(newUser.getProvider(), is(AuthProvider.GOOGLE));
    }

    @Test
    public void create_WillFailForNewUserFromOAuthProviderWithExistingEmail() {
        OAuthUserRequest userRequest = new OAuthUserRequest();
        userRequest.setProvider(AuthProvider.GOOGLE);
        userRequest.setExternalId("googleId");
        userRequest.setEmail("existing@example.org");
        userRequest.setName("Test User");

        LoginIdentifier existingIdentifier = new LoginIdentifier(user);
        when(loginIdentifierRepository.findById("existing@example.org")).thenReturn(Optional.of(existingIdentifier));

        thrown.expect(LoginIdentifierUnavailableException.class);
        thrown.expectMessage("Login ID existing@example.org is unavailable");

        userService.create(userRequest);
    }

    @Test
    public void update_willSucceed_whenUpdatingSelf() {
        String newEmail = "bar@example.org";
        Set<Role> roles = Sets.newHashSet();
        roles.add(Role.ADMIN);
        String newName = "Foo Bar";

        LoginIdentifier existingIdentifier = new LoginIdentifier(user);
        when(loginIdentifierRepository.findById(newEmail)).thenReturn(Optional.empty());

        UpdateUserRequest request = new UpdateUserRequest()
            .setEmail(newEmail)
            .grantRoles(roles)
            .setName(newName);

        User saved = userService.update(user.getId(), request);

        assertThat(saved, notNullValue());
        assertThat(saved.getId(), is(user.getId()));
        assertThat(saved.getEmail(), is(newEmail));
        assertThat(saved.getRoles(), is(roles));
        assertThat(saved.getName(), is(newName));

        verify(loginIdentifierRepository).delete(existingIdentifier.getLoginIdentifier());
        verify(loginIdentifierRepository).save(eq(new LoginIdentifier(saved)));
    }

    @Test
    public void update_willFail_whenEmailIsAlreadyInUse() {
        String newEmail = "existing@example.org";

        LoginIdentifier existingIdentifier = new LoginIdentifier(user);
        when(loginIdentifierRepository.findById(newEmail)).thenReturn(Optional.of(existingIdentifier));

        thrown.expect(LoginIdentifierUnavailableException.class);
        thrown.expectMessage("Login ID existing@example.org is unavailable");

        UpdateUserRequest request = new UpdateUserRequest()
            .setEmail(newEmail)
            .grantRoles(Collections.singletonList(Role.USER))
            .setName("New name");

        userService.update(user.getId(), request);
    }

    @Test
    public void list_willListAllUsers() {
        User user1 = User.byEmail("user1@email.com", "password");
        User user2 = User.byEmail("user2@email.com", "password");
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.list();

        assertThat(result, is(users));
    }
}
