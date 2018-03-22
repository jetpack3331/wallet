package hut34.wallet.framework.security;

import hut34.wallet.util.DateTimeUtils;
import org.springframework.security.web.header.HeaderWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Instant;

/**
 * Writes a header containing the session expiry so we can auto redirect the user
 * in the UI when the session expires.
 */
public class SessionExpiryHeaderWriter implements HeaderWriter {

    private static final String HEADER_SESSION_EXPIRY = "Session-Expiry";
    private static final String ATTRIBUTES_SESSION_UPDATE = "SessionUpdate";
    private static final int EXPIRY_BUFFER_SECONDS = 5;

    @Override
    public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Instant now = DateTimeUtils.now().toInstant();
            response.setHeader(HEADER_SESSION_EXPIRY, String.valueOf(calculateExpiryInstant(session, now)));
            updateTrackingAttribute(session, now);
        }
    }

    // Calculates expiry based on current time. We allow a small buffer
    // so that UI has chance to call logout before session is invalid.
    private long calculateExpiryInstant(HttpSession session, Instant now) {
        long currentTimeMillis = now.toEpochMilli();
        return currentTimeMillis + (session.getMaxInactiveInterval() - EXPIRY_BUFFER_SECONDS) * 1000;
    }

    // To save datastore writes the default App Engine Session manager only updates
    // the session expiry once 25% expired. We need our session expiry calculation
    // to be accurate so we force the session to be updated (by setting an attribute).
    // We use a second granularity to save datastore writes when multiple requests
    // come in together.
    private void updateTrackingAttribute(HttpSession session, Instant now) {
        String timeSeconds = String.valueOf(now.getEpochSecond());
        String sessionTimeSeconds = (String) session.getAttribute(ATTRIBUTES_SESSION_UPDATE);
        if (sessionTimeSeconds == null || !sessionTimeSeconds.equals(timeSeconds)) {
            session.setAttribute(ATTRIBUTES_SESSION_UPDATE, timeSeconds);
        }
    }
}
