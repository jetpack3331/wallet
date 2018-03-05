package hut34.wallet.framework.ref;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

public class ReferenceDataDto {
    private final String id;
    private final String description;
    private final int ordinal;
    private final Map<String, Object> props;

    public ReferenceDataDto(String id, String description, int ordinal, Object... propMapParams) {
        this.id = id;
        this.description = description;
        this.ordinal = ordinal;

        List<Object> list = asList(propMapParams);
        this.props = IntStream.range(1, list.size())
            .filter(i -> (i + 1) % 2 == 0)
            .mapToObj(i -> new SimpleEntry<>((String) list.get(i - 1), list.get(i)))
            .collect(Collectors.toConcurrentMap(SimpleEntry::getKey, SimpleEntry::getValue));
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public Map<String, Object> getProps() {
        return props;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProp(String key) {
        return (T) props.get(key);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}
