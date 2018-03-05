package hut34.wallet.framework.ref;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ReferenceDataServiceTest {

    @SuppressWarnings("unchecked")
    @Test
    public void getReferenceData_WillReturnMapOfReferenceData() {
        ReferenceDataConfig config = new ReferenceDataConfigBuilder()
            .registerClass(TestRef.class)
            .registerClass(AnotherTestRef.class)
            .create();

        ReferenceDataService service = new ReferenceDataService(config);

        Map<String, List<ReferenceDataDto>> referenceData = service.getReferenceData();
        assertThat(referenceData.size(), is(2));
        assertThat(referenceData.get("TestRef").size(), is(1));
        assertThat(referenceData.get("TestRef").get(0).getDescription(), is("TestRef 1"));
        assertThat(referenceData.get("AnotherTestRef").size(), is(2));
        assertThat(referenceData.get("AnotherTestRef").get(0).getDescription(), is("AnotherTestRef 1"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getReferenceData_WillUseCustomTransformersIfSpecified() {
        ReferenceDataConfig config = new ReferenceDataConfigBuilder()
            .registerClass(AnotherTestRef.class)
            .registerCustomTransformer(
                AnotherTestRef.class,
                ref -> new ReferenceDataDto(ref.name(), ref.getDescription(), ref.ordinal(), "displayText", ref.getDisplayText()))
            .create();

        ReferenceDataService service = new ReferenceDataService(config);

        Map<String, List<ReferenceDataDto>> referenceData = service.getReferenceData();
        assertThat(referenceData.size(), is(1));
        assertThat(referenceData.get("AnotherTestRef").get(0).getProp("displayText"), is("AnotherTestRef 1 display"));
        assertThat(referenceData.get("AnotherTestRef").get(1).getProp("displayText"), is("AnotherTestRef 2 display"));
    }

    private enum TestRef implements ReferenceData {
        REF_1("TestRef 1");

        private String description;

        TestRef(String description) {
            this.description = description;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    private enum AnotherTestRef implements ReferenceData {
        REF_1("AnotherTestRef 1", "AnotherTestRef 1 display"),
        REF_2("AnotherTestRef 2", "AnotherTestRef 2 display");

        private String description;
        private String displayText;

        AnotherTestRef(String description, String displayText) {
            this.description = description;
            this.displayText = displayText;
        }

        @Override
        public String getDescription() {
            return description;
        }

        public String getDisplayText() {
            return displayText;
        }
    }
}
