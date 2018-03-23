package hut34.wallet.filter;

import com.google.common.collect.Sets;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class ProtectedEnvironmentFilter extends GenericFilterBean implements PreauthFilter {
    public static final String PROTECTED_ENVIRONMENT_PATH = "/protected-environment";
    private static final String COOKIE_NAME = "hut-protected-environment";

    private final String secretPassword;
    private final String cookieValue;
    private final Set<String> excludedPaths;

    public ProtectedEnvironmentFilter(String secretPassword, String... excludedPaths) {
        this.secretPassword = secretPassword;
        this.cookieValue = DigestUtils.sha256Hex(secretPassword);
        this.excludedPaths = Sets.newHashSet(excludedPaths);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Cookie cookie = WebUtils.getCookie(request, COOKIE_NAME);

        if (!excludedPaths.contains(request.getRequestURI()) && (cookie == null || !cookieValue.equals(cookie.getValue()))) {
            response.sendRedirect(request.getContextPath() + PROTECTED_ENVIRONMENT_PATH);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    /**
     * Not strictly for Filter, but keeping functionality together for secret password config and cookie stuff for protected environment.
     */
    public boolean passwordMatches(String password) {
        return secretPassword.equals(password);
    }

    /**
     * Not strictly for Filter, but keeping functionality together for secret password config and cookie stuff for protected environment.
     */
    public Cookie newCookie() {
        return new Cookie(COOKIE_NAME, cookieValue);
    }

}
