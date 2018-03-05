package hut34.wallet.framework.service.bootstrap;

import com.googlecode.objectify.ObjectifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BootstrapService {
    private static final Logger LOG = LoggerFactory.getLogger(BootstrapService.class);

    private final List<Bootstrapper> bootstrappers;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public BootstrapService(Optional<List<Bootstrapper>> bootstrappers) {
        this.bootstrappers = bootstrappers.orElse(Collections.emptyList());
    }

    public void bootstrap() {
        LOG.info("Bootstrapping...");
        LOG.debug("Running {} bootstrapper{}", bootstrappers.size(), bootstrappers.size() == 1 ? "" : "s");

        ObjectifyService.run(() -> {
            bootstrappers.forEach(b -> {
                LOG.debug("Running Bootstrapper: {}", b.getClass().getSimpleName());
                b.bootstrap();
            });
            return null;
        });

        LOG.info("Bootstrapping complete.");
    }
}
