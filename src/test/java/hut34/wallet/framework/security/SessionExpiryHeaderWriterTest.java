package hut34.wallet.framework.security;

import hut34.wallet.util.DateTimeUtils;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SessionExpiryHeaderWriterTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private SessionExpiryHeaderWriter sessionExpiryHeaderWriter;

    @After
    public void tearDown() {
        DateTimeUtils.setClockSystem();
    }

    @Test
    public void writeHeaders_WillWriteExpiryTimeHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        session.setMaxInactiveInterval(600);
        request.setSession(session);
        MockHttpServletResponse response = new MockHttpServletResponse();

        DateTimeUtils.setClockTime(OffsetDateTime.parse("2018-03-12T01:16:38.337Z"));

        sessionExpiryHeaderWriter.writeHeaders(request, response);

        assertThat(response.containsHeader("Session-Expiry"), is(true));
        assertThat(response.getHeader("Session-Expiry"), is(String.valueOf(1520817998337L)));
    }
}
