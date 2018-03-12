package hut34.wallet.framework.security;

import hut34.wallet.util.DateTimeUtils;
import org.springframework.security.web.header.HeaderWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Writes a header containing the session expiry so we can auto redirect the user
 * in the UI when the session expires.
 */
public class SessionExpiryHeaderWriter implements HeaderWriter {

    private static final String HEADER_SESSION_EXPIRY = "Session-Expiry";

    @Override
    public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            response.setHeader(HEADER_SESSION_EXPIRY, String.valueOf(calculateExpiryInstant(session)));
        }
    }

    private long calculateExpiryInstant(HttpSession session) {
        long currentTimeMillis = DateTimeUtils.now().toInstant().toEpochMilli();
        return currentTimeMillis + session.getMaxInactiveInterval() * 1000;
    }
}
