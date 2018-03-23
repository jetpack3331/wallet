package hut34.wallet.controller;


import hut34.wallet.filter.ProtectedEnvironmentFilter;
import hut34.wallet.testinfra.BaseControllerTest;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;

import javax.servlet.http.Cookie;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProtectedEnvironmentControllerTest extends BaseControllerTest {

    @Mock
    private ProtectedEnvironmentFilter filter;

    @Override
    protected Object controller() {
        return new ProtectedEnvironmentController(filter);
    }

    @Test
    public void authenticate() throws Exception {
        Cookie cookie = new Cookie("cookie", "yummmm");
        when(filter.passwordMatches("my-password")).thenReturn(true);
        when(filter.newCookie()).thenReturn(cookie);

        mvc.perform(
            post("/protected-auth")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("password", "my-password"))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/"))
            .andExpect(cookie().value(cookie.getName(), cookie.getValue()));
    }

    @Test
    public void authenticate_willRedirect_butNotSetCookie_whenPasswordDoesNotMatch() throws Exception {
        when(filter.passwordMatches("my-password")).thenReturn(false);

        mvc.perform(
            post("/protected-auth")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("password", "my-password"))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/"));

        verify(filter, never()).newCookie();
    }


}
