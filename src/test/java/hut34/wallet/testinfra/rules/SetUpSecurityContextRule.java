package hut34.wallet.testinfra.rules;

import org.junit.rules.ExternalResource;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import hut34.wallet.framework.usermanagement.dto.AuthUser;

import static org.mockito.Mockito.when;

public class SetUpSecurityContextRule extends ExternalResource {
    private AuthUser loggedIn;

    public SetUpSecurityContextRule(AuthUser loggedIn) {
        this.loggedIn = loggedIn;
    }

    protected void before() {
        initializeSecurityContext(loggedIn);
    }

    protected void after() {
    }

    private void initializeSecurityContext(AuthUser authUser) {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(authUser);
    }

}
