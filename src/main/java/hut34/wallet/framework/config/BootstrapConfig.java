package hut34.wallet.framework.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import hut34.wallet.framework.service.bootstrap.BootstrapService;

@Configuration
public class BootstrapConfig {

    @Bean
    public ApplicationListener<ApplicationReadyEvent> bootstrapOnReady(BootstrapService bootstrapService) {
        return applicationReadyEvent -> bootstrapService.bootstrap();
    }
}
