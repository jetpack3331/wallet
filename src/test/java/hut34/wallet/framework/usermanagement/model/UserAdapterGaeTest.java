package hut34.wallet.framework.usermanagement.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import hut34.wallet.framework.usermanagement.service.UserService;
import hut34.wallet.testinfra.rules.LocalServicesRule;
import hut34.wallet.testinfra.rules.SetUpSecurityContextRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAdapterGaeTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    @Rule
    public SetUpSecurityContextRule setUpSecurityContextRule = new SetUpSecurityContextRule();
    @Rule
    public LocalServicesRule localServicesRule = new LocalServicesRule();

    private UserAdapterGae userAdapter;

    @Mock
    private UserService userService;

    @Before
    public void before() {
        userAdapter = UserAdapterGae.byEmail(userService);
    }

    @Test
    public void getCurrentUserKey() {
        Optional<Key<User>> result = UserAdapterGae.currentUserKey();

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getName(), is(setUpSecurityContextRule.getUser().getId()));
    }

    @Test
    public void getCurrentUserKey_willReturnEmpty_whenSecurityContextCleared() {
        SecurityContextHolder.clearContext();

        Optional<Key<User>> result = UserAdapterGae.currentUserKey();

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void getCurrentUserKey_willReturnEmpty_whenUserTypeDoesNotMatch() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn("not a user object");

        Optional<Key<User>> result = UserAdapterGae.currentUserKey();

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void getCurrentUserRef() {
        Optional<Ref<User>> result = UserAdapterGae.currentUserRef();

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getKey().getName(), is(setUpSecurityContextRule.getUser().getId()));
    }

    @Test
    public void getCurrentUserRef_willReturnEmpty_whenSecurityContextCleared() {
        SecurityContextHolder.clearContext();

        Optional<Ref<User>> result = UserAdapterGae.currentUserRef();

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void getCurrentUser() {
        when(userService.getById(setUpSecurityContextRule.getUserId()))
            .thenReturn(Optional.of(setUpSecurityContextRule.getUser()));

        Optional<User> result = userAdapter.getCurrentUser();

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(setUpSecurityContextRule.getUser()));
    }

    @Test
    public void getCurrentUser_willReturnEmpty_whenUserNotInDatabase() {
        when(userService.getById(setUpSecurityContextRule.getUserId()))
            .thenReturn(Optional.empty());

        Optional<User> result = userAdapter.getCurrentUser();

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void getCurrentUser_willReturnEmpty_whenSecurityContextCleared() {
        SecurityContextHolder.clearContext();

        Optional<User> result = userAdapter.getCurrentUser();

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void getCurrentUserRequired() {
        when(userService.getById(setUpSecurityContextRule.getUserId()))
            .thenReturn(Optional.of(setUpSecurityContextRule.getUser()));

        User result = userAdapter.getCurrentUserRequired();

        assertThat(result, is(setUpSecurityContextRule.getUser()));
    }

    @Test
    public void getCurrentUserRequired_willError_whenNoCurrentUser() {
        SecurityContextHolder.clearContext();

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Authenticated user required");
        userAdapter.getCurrentUserRequired();
    }

}
