package hut34.wallet.framework.ref;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ReferenceDataConfig {
    private List<Class<? extends ReferenceData>> registeredClasses;
    private Map<Class<? extends ReferenceData>, Function<? extends ReferenceData, ReferenceDataDto>> customTransformers;

    public ReferenceDataConfig(List<Class<? extends ReferenceData>> registeredClasses, Map<Class<? extends ReferenceData>, Function<? extends ReferenceData, ReferenceDataDto>> customTransformers) {
        this.registeredClasses = registeredClasses;
        this.customTransformers = customTransformers;
    }

    public List<Class<? extends ReferenceData>> getRegisteredClasses() {
        return registeredClasses;
    }

    public Map<Class<? extends ReferenceData>, Function<? extends ReferenceData, ReferenceDataDto>> getCustomTransformers() {
        return customTransformers;
    }
}
