package hut34.wallet.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.contrib.gae.objectify.config.ObjectifyConfigurer;
import org.springframework.contrib.gae.objectify.config.ObjectifyEntityScanner;

import java.util.Collection;

@Configuration
public class ObjectifyConfig implements ObjectifyConfigurer {

    @Override
    public Collection<Class<?>> registerObjectifyEntities() {
        return new ObjectifyEntityScanner("hut34.**.model")
            .getEntityClasses();
    }
}
