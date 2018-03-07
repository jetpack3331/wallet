package hut34.wallet.framework;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import hut34.wallet.testinfra.BaseIntegrationTest;
import hut34.wallet.testinfra.rules.FixedClock;
import hut34.wallet.util.DateTimeUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;

import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class BaseEntityCoreIntegrationTest extends BaseIntegrationTest {
    @Rule
    public FixedClock fixedClock = new FixedClock();

    @Before
    public void before() {
        localServicesRule.registerAdditionalEntities(TestCoreEntity.class);
    }

    @Test
    public void willSetCreatedAndUpdated_whenEntityCreated() {

        TestCoreEntity result = save(new TestCoreEntity());

        assertThat(result.getCreated(), is(DateTimeUtils.now()));
        assertThat(result.getUpdated(), is(DateTimeUtils.now()));
    }

    @Test
    public void willSetUpdatedButNotOverrideCreated_whenBothAlreadySet() {
        TestCoreEntity existing = new TestCoreEntity();
        OffsetDateTime original = OffsetDateTime.now().minusYears(1);
        ReflectionTestUtils.setField(existing, "created", original);
        ReflectionTestUtils.setField(existing, "updated", original);

        TestCoreEntity result = save(existing);

        assertThat(result.getCreated(), is(original));
        assertThat(result.getUpdated(), is(DateTimeUtils.now()));
    }

    @Test
    public void willNotSetEitherField_whenSkipSettingAuditableFields() {
        TestCoreEntity entity = new TestCoreEntity();
        entity.skipSettingAuditableFields();

        TestCoreEntity result = save(entity);

        assertThat(result.getCreated(), nullValue());
        assertThat(result.getUpdated(), nullValue());
    }

    @Entity
    public static class TestCoreEntity extends BaseEntityCore {
        @Id
        private String id;

        public TestCoreEntity() {
            this.id = randomUUID().toString();
        }
    }


}
