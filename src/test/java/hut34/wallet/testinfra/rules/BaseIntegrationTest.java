package hut34.wallet.testinfra.rules;

import hut34.wallet.framework.usermanagement.dto.AuthUser;
import hut34.wallet.framework.usermanagement.model.LoginIdentifier;
import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.testinfra.TestApplicationContext;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(properties = { "app.host=http://testinghost:8080" })
@ContextConfiguration(classes = TestApplicationContext.class)
@SpringBootTest
public abstract class BaseIntegrationTest {
    protected User loggedIn = User.byEmail("admin@3wks.com.au", "myPass");

    @Rule
    public SetUpSecurityContextRule setUpSecurityContextRule =
        new SetUpSecurityContextRule(new AuthUser(loggedIn.getId(), "andres", "passw", Collections.emptyList()));


    @Rule
    public LocalServicesRule localServicesRule = new LocalServicesRule(User.class, LoginIdentifier.class);

}
