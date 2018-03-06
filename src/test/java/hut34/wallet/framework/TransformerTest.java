package hut34.wallet.framework;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("ConstantConditions")
public class TransformerTest {

    private StringToLength transformer = new StringToLength();


    @Test
    public void transform() {
        assertThat(transformer.transform("abc"), is(3));
    }

    @Test
    public void transform_Optional() {
        Optional<Integer> result = transformer.transform(Optional.of("abc"));

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(3));
    }

    @Test
    public void transform_List() {
        List<Integer> result = transformer.transform(Arrays.asList("a", "ab", "abc"));

        assertThat(result, contains(1, 2, 3));
    }

    @Test
    public void transform_Set() {
        Set<Integer> result = transformer.transform(Sets.newHashSet("a", "ab", "abc"));

        assertThat(result, containsInAnyOrder(1, 2, 3));
    }

    @Test
    public void transform_CustomCollection() {
        LinkedHashSet<Integer> result = transformer.transform(Arrays.asList("a", "ab", "abc"), LinkedHashSet::new);

        assertThat(result, contains(1, 2, 3));
    }

    @Test
    public void tranform_willWorkInStream() {
        List<Integer> result = Stream.of("a", "ab", "abc")
            .map(transformer)
            .collect(Collectors.toList());

        assertThat(result, contains(1, 2, 3));
    }

    public static class StringToLength implements Transformer<String, Integer> {
        @Override
        public Integer apply(String s) {
            return s.length();
        }
    }

}
