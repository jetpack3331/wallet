package hut34.wallet.testinfra;


import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Test controller via {@link MockMvc} without requiring a full container load - faster. If you want to test
 * Spring Security, use {@link BaseControllerIntegrationTest} which sets up web application context.
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class BaseControllerTest {

    protected MockMvc mvc;

    protected abstract Object controller();

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller())
                .alwaysDo(print())
                .build();
    }

}
