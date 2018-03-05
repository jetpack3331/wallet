package hut34.wallet.framework.ref;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReferenceDataService {

    private final Map<Class<? extends ReferenceData>, Function<? extends ReferenceData, ReferenceDataDto>> customTransformers;
    private Map<String, List<ReferenceDataDto>> referenceData = new HashMap<>();

    public ReferenceDataService(ReferenceDataConfig config) {
        customTransformers = config.getCustomTransformers();
        config.getRegisteredClasses().forEach(referenceDataClass -> {
            List<ReferenceDataDto> transform = transform(referenceDataClass);
            referenceData.put(referenceDataClass.getSimpleName(), transform);
        });
    }

    private <T extends ReferenceData> List<ReferenceDataDto> transform(Class<T> referenceDataClass) {
        return getEnumConstants(referenceDataClass)
            .stream()
            .map(getTransformer(referenceDataClass))
            .collect(Collectors.toList());
    }

    private <T extends ReferenceData> List<T> getEnumConstants(Class<T> referenceDataClass) {
        return Arrays.asList(referenceDataClass.getEnumConstants());
    }

    @SuppressWarnings("unchecked")
    private <T extends ReferenceData> Function<T, ReferenceDataDto> getTransformer(Class<T> referenceDataClass) {
        Optional transformer = customTransformers
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey().isAssignableFrom(referenceDataClass))
            .map(Map.Entry::getValue)
            .findFirst();
        return (Function<T, ReferenceDataDto>) transformer.orElse(ReferenceData.TO_DTO_TRANSFORMER);
    }

    public Map<String, List<ReferenceDataDto>> getReferenceData() {
        return referenceData;
    }
}
