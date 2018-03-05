package hut34.wallet.testinfra;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import hut34.wallet.Application;

@Configuration
@Import(Application.class)
public class TestApplicationContext {
}
