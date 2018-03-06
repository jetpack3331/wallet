package hut34.wallet.framework.usermanagement;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import hut34.wallet.framework.BaseEntity;
import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.testinfra.TestData;
import hut34.wallet.testinfra.rules.BaseIntegrationTest;
import hut34.wallet.testinfra.rules.SecurityContextRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.contrib.gae.objectify.Refs;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;

import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class BaseEntityIntegrationTest extends BaseIntegrationTest {
    @Rule
    public SecurityContextRule securityContextRule = new SecurityContextRule();

    private User user;

    @Before
    public void before() {
        localServicesRule.registerAdditionalEntities(TestEntity.class);
        user = userRepository.save(securityContextRule.getUser());
    }


    @Test
    public void willSetCreatedByAndUpdatedBy_whenEntityCreated() {

        TestEntity result = save(new TestEntity());

        assertThat(result.getCreatedBy(), is(user));
        assertThat(result.getUpdatedBy(), is(user));
    }

    @Test
    public void willNotSetCreatedByAndUpdatedBy_whenNoUserInSecurityContext() {
        SecurityContextHolder.clearContext();

        TestEntity result = save(new TestEntity());

        assertThat(result.getCreatedBy(), nullValue());
        assertThat(result.getUpdatedBy(), nullValue());
    }

    @Test
    public void willSetUpdatedByButNotOverrideCreatedBy_whenAlreadyCreated() {
        TestEntity existing = new TestEntity();
        OffsetDateTime original = OffsetDateTime.now();
        User originalUser = userRepository.save(TestData.user("some-other@email.org"));

        ReflectionTestUtils.setField(existing, "created", original);
        ReflectionTestUtils.setField(existing, "createdBy", Refs.ref(originalUser));
        ReflectionTestUtils.setField(existing, "updated", original);
        ReflectionTestUtils.setField(existing, "updatedBy", Refs.ref(originalUser));

        TestEntity result = save(existing);

        assertThat(result.getCreatedBy(), is(originalUser));
        assertThat(result.getUpdatedBy(), is(user));
    }


    @Test
    public void willNotSetCreatedBy_whenAlreadyCreatedWithNullCreatedBy() {
        // This scenario happens if the entity was created by an unauthenticated user. We don't want to override that.
        TestEntity existing = new TestEntity();
        OffsetDateTime original = OffsetDateTime.now();

        ReflectionTestUtils.setField(existing, "created", original);
        ReflectionTestUtils.setField(existing, "updated", original);

        TestEntity result = save(existing);

        assertThat(result.getCreatedBy(), nullValue());
        assertThat(result.getUpdatedBy(), is(user));
    }

    @Test
    public void willNotSetEitherField_whenSkipSettingAuditableFields() {
        TestEntity entity = new TestEntity();
        entity.skipSettingAuditableFields();

        TestEntity result = save(entity);

        assertThat(result.getCreatedBy(), nullValue());
        assertThat(result.getUpdatedBy(), nullValue());
    }

    @Entity
    public static class TestEntity extends BaseEntity {
        @Id
        private String id;

        public TestEntity() {
            this.id = randomUUID().toString();
        }
    }


}
