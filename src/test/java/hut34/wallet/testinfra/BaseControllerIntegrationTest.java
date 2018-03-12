package hut34.wallet.testinfra;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Use this base test when you specifically want to test integration with spring security features. If you want to test
 * a standalone controller using {@link MockMvc} then extend {@link BaseControllerTest}.
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("junit")
@SpringBootTest
public abstract class BaseControllerIntegrationTest {

    protected MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
            .webAppContextSetup(wac)
            .apply(springSecurity())
            .build();
    }
}
