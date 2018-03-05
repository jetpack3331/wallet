package hut34.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.OnSave;
import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.framework.usermanagement.model.UserAdapterImpl;
import org.springframework.contrib.gae.objectify.Refs;
import org.springframework.contrib.gae.search.IndexType;
import org.springframework.contrib.gae.search.SearchIndex;

import java.time.OffsetDateTime;

/**
 * Created and updated timestamps set. On creation updated and created are the same.
 * <p>
 */
public abstract class BaseEntity {

    /**
     * A marker interface to allow us to specify when to pre-fetch user Ref entities.
     * See https://github.com/objectify/objectify/wiki/BasicOperations#load-groups for how to do this.
     */
    public interface PreLoadAuditRefs {
    }

    public static class Fields {
        public static final String updated = "updated";
        public static final String updatedBy = "updatedBy";
        public static final String created = "created";
        public static final String createdBy = "createdBy";
    }

    @Index
    @SearchIndex(type = IndexType.NUMBER)
    private OffsetDateTime created;

    // Only pre-load when specifically asked, using the marker interface
    @Load(PreLoadAuditRefs.class)
    @Index
    @SearchIndex(type = IndexType.IDENTIFIER)
    private Ref<User> createdBy;

    @Index
    @SearchIndex(type = IndexType.NUMBER)
    private OffsetDateTime updated;

    // Only pre-load when specifically asked, using the marker interface
    @Load(PreLoadAuditRefs.class)
    @Index
    @SearchIndex(type = IndexType.IDENTIFIER)
    private Ref<User> updatedBy;

    @Ignore
    private transient boolean skipSettingAuditableFields = false;

    /**
     * Handy for data migrations where you don't want to overwrite last updated.
     */
    public void skipSettingAuditableFields() {
        skipSettingAuditableFields = true;
    }

    @JsonIgnore
    public boolean isSkipSettingAuditableFields() {
        return skipSettingAuditableFields;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    @JsonIgnore
    public User getCreatedBy() {
        return Refs.deref(createdBy);
    }

    @JsonIgnore
    public Key<User> getCreatedByKey() {
        return toKey(createdBy);
    }

    public OffsetDateTime getUpdated() {
        return updated;
    }

    @JsonIgnore
    public User getUpdatedBy() {
        return Refs.deref(updatedBy);
    }

    //    @JsonIgnore
    public Key<User> getUpdatedByKey() {
        return toKey(updatedBy);
    }

    @OnSave
    private void setAuditableFieldsOnSave() {
        if (!skipSettingAuditableFields) {
            updated = OffsetDateTime.now();
            updatedBy = getCurrentUserRef();

            if (created == null) {
                created = updated;
                createdBy = updatedBy;
            }
        }
    }

    private Key<User> toKey(Ref<User> ref) {
        return ref == null ? null : ref.getKey();
    }

    private Ref<User> getCurrentUserRef() {
        return UserAdapterImpl.currentUserRef()
            .orElse(null);
    }

}
