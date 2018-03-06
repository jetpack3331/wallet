package hut34.wallet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.contrib.gae.config.helper.ProfileExtractors;
import org.springframework.contrib.gae.config.helper.ProfileResolver;

import java.util.List;

public class ServletInitializer extends SpringBootServletInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(ServletInitializer.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        List<String> profiles = new ProfileResolver()
            // Set a profile that allows us to identify all gae environments
            .setAdditionalProfileExtractor(ProfileExtractors.staticValue("gae"))
            // Allow us to reference "dev", "uat" without full app id
            .setAdditionalProfileExtractor(ProfileExtractors.AFTER_LAST_DASH)
            .getProfiles();

        if (profiles.contains("local")) {
            profiles.add("hut34-wallet-local");
        }

        LOG.info("Setting profiles: {}", profiles);
        return application.sources(Application.class)
            .profiles(profiles.toArray(new String[profiles.size()]));
    }

}
