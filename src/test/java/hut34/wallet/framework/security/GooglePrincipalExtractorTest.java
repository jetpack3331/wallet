package hut34.wallet.framework.security;

import hut34.wallet.framework.usermanagement.dto.AuthUser;
import hut34.wallet.framework.usermanagement.dto.OAuthUserRequest;
import hut34.wallet.framework.usermanagement.model.AuthProvider;
import hut34.wallet.framework.usermanagement.service.UserService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.HashMap;
import java.util.Map;

import static hut34.wallet.framework.usermanagement.model.User.byEmail;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GooglePrincipalExtractorTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private UserService userService;
    @Mock
    private UserDetailsManager userDetailsManager;

    @InjectMocks
    private GooglePrincipalExtractor principalExtractor;

    @Test
    public void extractPrincipal_WillExtractAndLoadExistingUser() {
        when(userService.get("bob@email.com")).thenReturn(of(byEmail("bob@email.com", "password")));
        UserDetails userDetails = new AuthUser("id", "bob@email.com", "password", emptyList());
        when(userDetailsManager.loadUserByUsername("bob@email.com")).thenReturn(userDetails);

        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("email", "bob@email.com");
        infoMap.put("name", "Bob");
        infoMap.put("sub", "googleId");

        UserDetails principal = (UserDetails) principalExtractor.extractPrincipal(infoMap);

        assertThat(principal.getUsername(), is("bob@email.com"));
        assertThat(principal.isEnabled(), is(true));
        verify(userService, never()).create(any(OAuthUserRequest.class));
    }

    @Test
    public void extractPrincipal_WillAutoRegisterNewUser() {
        when(userService.get("bob@email.com")).thenReturn(empty());
        UserDetails userDetails = new AuthUser("id", "bob@email.com", "password", emptyList());
        when(userDetailsManager.loadUserByUsername("bob@email.com")).thenReturn(userDetails);

        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("email", "bob@email.com");
        infoMap.put("name", "Bob");
        infoMap.put("sub", "googleId");

        UserDetails principal = (UserDetails) principalExtractor.extractPrincipal(infoMap);

        assertThat(principal.getUsername(), is("bob@email.com"));

        ArgumentCaptor<OAuthUserRequest> argument = ArgumentCaptor.forClass(OAuthUserRequest.class);
        verify(userService).create(argument.capture());
        assertThat(argument.getValue().getExternalId(), is("googleId"));
        assertThat(argument.getValue().getProvider(), is(AuthProvider.GOOGLE));
        assertThat(argument.getValue().getEmail(), is("bob@email.com"));
        assertThat(argument.getValue().getName(), is("Bob"));
    }
}
