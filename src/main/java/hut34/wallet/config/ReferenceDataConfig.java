package hut34.wallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import hut34.wallet.framework.ref.ReferenceDataConfigBuilder;
import hut34.wallet.framework.ref.ReferenceDataService;

@Configuration
public class ReferenceDataConfig {

    @Bean
    public ReferenceDataService referenceDataService() {
        hut34.wallet.framework.ref.ReferenceDataConfig config = new ReferenceDataConfigBuilder()
            // Add your reference data classes here
            .create();

        return new ReferenceDataService(config);
    }
}
