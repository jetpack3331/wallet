package hut34.wallet.framework.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.googlecode.objectify.Key;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

import static org.springframework.contrib.gae.util.Nulls.ifNotNull;

/**
 * We shouldn't really be exposing Objectify {@link com.googlecode.objectify.Ref} and {@link Key} in API, but if we do by
 * accident this will stop things crashing and provide a sensible default.
 *
 * Best to exclude any getters with {@link com.fasterxml.jackson.annotation.JsonIgnore} annotation.
 */
@JsonComponent
public class KeySerializer extends JsonSerializer<Key> {

    @Override
    public Class<Key> handledType() {
        return Key.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void serialize(Key value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String websafeString = ifNotNull(value, Key::toWebSafeString);
        gen.writeString(websafeString);
    }

}
