package hut34.wallet.testinfra;

import hut34.wallet.testinfra.rules.LocalServicesRule;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Rule
    public LocalServicesRule localServicesRule = new LocalServicesRule();
}
