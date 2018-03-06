package hut34.wallet.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Extension of {@link Function} with added default convenience methods for transforming variations of
 * objects. As it extends {@link Function}, instances of this interface may be used in streams.**
 *
 *
 * @param <F> From type
 * @param <T> To type
 */
public interface Transformer<F, T> extends Function<F, T> {

    default T transform(F from) {
        return apply(from);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    default Optional<T> transform(Optional<F> from) {
        return from.map(this);
    }

    default List<T> transform(List<F> from) {
        return transform(from, ArrayList::new);
    }

    default Set<T> transform(Set<F> from) {
        return transform(from, HashSet::new);
    }

    /**
     * Transform a {@link Collection} and collect it to any implementation, by supplying the construction method.
     * <p>
     *     Example:
     * <blockquote>
     * <pre>{@code
     * List<String> myCollection = Arrays.asList("one", "two");
     * transformer.transform(myCollection, LinkedHashSet::new);
     * transformer.transform(myCollection, LinkedList::new);
     * }</pre>
     * </blockquote>
     * </p>
     * @param from source collection
     * @param collectionFactory a {@code Supplier} which returns a new, empty {@code Collection} of the appropriate type
     * @param <COLL> the {@code Collection} type returned
     * @return The transformed collection
     */
    default <COLL extends Collection<T>> COLL transform(Collection<? extends F> from, Supplier<COLL> collectionFactory) {
        return from.stream()
            .map(this)
            .collect(Collectors.toCollection(collectionFactory));
    }

}
