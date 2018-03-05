package hut34.wallet.framework.ref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ReferenceDataConfigBuilder {
    private List registeredClasses = new ArrayList<Class<? extends ReferenceData>>();
    private final Map<Class<? extends ReferenceData>, Function<? extends ReferenceData, ReferenceDataDto>> customTransformers = new HashMap<>();

    @SuppressWarnings("unchecked")
    public ReferenceDataConfigBuilder registerClass(Class<? extends ReferenceData> referenceDataClass) {
        registeredClasses.add(referenceDataClass);
        return this;
    }

    public <R extends ReferenceData> ReferenceDataConfigBuilder registerCustomTransformer(Class<R> referenceDataClass, Function<R, ReferenceDataDto> transformer) {
        customTransformers.put(referenceDataClass, transformer);
        return this;
    }

    @SuppressWarnings("unchecked")
    public ReferenceDataConfig create() {
        return new ReferenceDataConfig(registeredClasses, customTransformers);
    }
}
