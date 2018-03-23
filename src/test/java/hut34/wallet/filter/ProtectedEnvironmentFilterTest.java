package hut34.wallet.filter;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProtectedEnvironmentFilterTest {
    private static final String SECRET_PASSWORD = "secret-password";
    private static final String EXCLUDED_PATH = "/excluded-path";

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    private ProtectedEnvironmentFilter filter;

    @Before
    public void before() {
        filter = new ProtectedEnvironmentFilter(SECRET_PASSWORD, EXCLUDED_PATH, "/some-other-excluded-path");
    }

    @Test
    public void passwordMatches() {
        assertThat(filter.passwordMatches(SECRET_PASSWORD), is(true));
        assertThat(filter.passwordMatches("bad"), is(false));
    }

    @Test
    public void newCookie() {
        Cookie cookie = filter.newCookie();

        assertThat(cookie.getName(), is("hut-protected-environment"));
        assertThat(cookie.getValue(), is(DigestUtils.sha256Hex(SECRET_PASSWORD)));
    }

    @Test
    public void doFilter_willRedirect_whenCookieDoesNotExist() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/some-request-path");
        when(request.getContextPath()).thenReturn("");
        when(request.getCookies()).thenReturn(new Cookie[0]);

        filter.doFilter(request, response, filterChain);

        verify(response).sendRedirect("/protected-environment");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    public void doFilter_willRedirect_whenCookieValueDoesNotMatchExpected() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/some-request-path");
        when(request.getContextPath()).thenReturn("");
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("hut-protected-environment", "bad value")});

        filter.doFilter(request, response, filterChain);

        verify(response).sendRedirect("/protected-environment");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    public void doFilter_willNotRedirect_whenExcludedPath() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn(EXCLUDED_PATH);
        when(request.getCookies()).thenReturn(new Cookie[0]);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(response, never()).sendRedirect("/protected-environment");
    }

    @Test
    public void doFilter_willNotRedirect_whenValidCookieExists() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/some-request-path");
        when(request.getContextPath()).thenReturn("");
        when(request.getCookies()).thenReturn(new Cookie[]{filter.newCookie()});

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(response, never()).sendRedirect("/protected-environment");
    }
}
