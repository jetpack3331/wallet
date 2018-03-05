package hut34.wallet.framework.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.contrib.gae.objectify.Refs;

import java.io.IOException;

import static org.springframework.contrib.gae.util.Nulls.ifNotNull;

/**
 * We shouldn't really be exposing Objectify {@link Ref} and {@link Key} in API, but if we do by
 * accident this will stop things crashing and provide a sensible default.
 *
 * Best to exclude any getters with {@link com.fasterxml.jackson.annotation.JsonIgnore} annotation.
 */
@JsonComponent
public class RefSerializer extends JsonSerializer<Ref> {

    @Override
    public Class<Ref> handledType() {
        return Ref.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void serialize(Ref value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String websafeString = ifNotNull(Refs.key(value), Key::toWebSafeString);
        gen.writeString(websafeString);
    }
}
