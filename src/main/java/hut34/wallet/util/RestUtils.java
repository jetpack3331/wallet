package hut34.wallet.util;

import java.util.Optional;

public interface RestUtils {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static <T> T response(Optional<T> optionalResponse) {
        return optionalResponse.orElseThrow(NotFoundException::new);
    }

}
