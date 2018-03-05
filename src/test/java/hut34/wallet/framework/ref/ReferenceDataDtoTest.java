package hut34.wallet.framework.ref;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReferenceDataDtoTest {
    @Test
    public void constructor_WillPopulateProps_FromPropMapParams() {
        ReferenceDataDto referenceDataDto = new ReferenceDataDto("id", "desc", 1, "key1", 1, "key2", "key-2-value");
        assertThat(referenceDataDto.getProps().size(), is(2));
        assertThat(referenceDataDto.getProps().get("key1"), is(1));
        assertThat(referenceDataDto.getProps().get("key2"), is("key-2-value"));
    }

    @Test
    public void constructor_CanHandle_NoPropMapParams() {
        ReferenceDataDto referenceDataDto = new ReferenceDataDto("id", "desc", 1);
        assertThat(referenceDataDto.getProps().size(), is(0));
    }
}
