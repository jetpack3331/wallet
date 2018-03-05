package hut34.wallet.testinfra;

import org.springframework.contrib.gae.objectify.repository.SaveRepository;
import org.springframework.data.repository.Repository;

import java.util.Collection;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class MockHelpers {

    /**
     * Mocks the {@link Repository} behaviour of returning the same entity when a {@link SaveRepository#save(Object)}
     * method is called and also returning the same {@link Collection <T>} when a list is put.
     */
    @SuppressWarnings("unchecked")
    public static <T> void mockSave(SaveRepository<T, ?> repository, Class<T> entityType) {
        returnFirstArgument(repository.save(any(entityType)));
        returnFirstArgument(repository.save(any(Collection.class)));
    }

    /**
     * Convenience for returning the first argument of any method call on a mock.
     * Example usage
     * <code>
     *     returnFirstArgument(myMethod(param1));
     *     returnFirstArgument(myMethod(any(String.class));
     * </code>
     */
    public static <T> void returnFirstArgument(T methodCall) {
        when(methodCall).thenAnswer(invocation -> invocation.getArguments()[0]);
    }

}
