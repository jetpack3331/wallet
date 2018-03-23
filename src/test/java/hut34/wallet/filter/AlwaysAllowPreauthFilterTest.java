package hut34.wallet.filter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AlwaysAllowPreauthFilterTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    private AlwaysAllowPreauthFilter filter = new AlwaysAllowPreauthFilter();

    @Test
    public void doFilter() throws IOException, ServletException {
        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

}
