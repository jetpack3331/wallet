package hut34.wallet.testinfra;

import hut34.wallet.framework.usermanagement.repository.UserRepository;
import hut34.wallet.testinfra.rules.LocalServicesRule;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.googlecode.objectify.ObjectifyService.ofy;

@RunWith(SpringRunner.class)
@ActiveProfiles("junit")
@ContextConfiguration(classes = TestApplicationContext.class)
@SpringBootTest
public abstract class BaseIntegrationTest {
    @Autowired
    protected UserRepository userRepository;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Rule
    public LocalServicesRule localServicesRule = new LocalServicesRule();


    @SuppressWarnings("unchecked")
    protected <E> E save(E entity) {
        ofy().save().entities(entity).now();
        return entity;
    }

}
