package hut34.wallet.framework;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.framework.usermanagement.model.UserAdapterGae;
import org.springframework.contrib.gae.objectify.Refs;
import org.springframework.contrib.gae.search.IndexType;
import org.springframework.contrib.gae.search.SearchIndex;

/**
 * Extension of {@link BaseEntityCore} to include who created and updated last if there was an authenticated user.
 */
public abstract class BaseEntity extends BaseEntityCore {

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

    // Only pre-load when specifically asked, using the marker interface
    @Load(PreLoadAuditRefs.class)
    @Index
    @SearchIndex(type = IndexType.IDENTIFIER)
    private Ref<User> createdBy;

    // Only pre-load when specifically asked, using the marker interface
    @Load(PreLoadAuditRefs.class)
    @Index
    @SearchIndex(type = IndexType.IDENTIFIER)
    private Ref<User> updatedBy;
    @JsonIgnore
    public User getCreatedBy() {
        return Refs.deref(createdBy);
    }

    @JsonIgnore
    public Key<User> getCreatedByKey() {
        return toKey(createdBy);
    }

    @JsonIgnore
    public User getUpdatedBy() {
        return Refs.deref(updatedBy);
    }

    @JsonIgnore
    public Key<User> getUpdatedByKey() {
        return toKey(updatedBy);
    }

    @Override
    protected void onCreated() {
        super.onCreated();
        createdBy = getCurrentUserRef();
    }

    @Override
    protected void onUpdated() {
        super.onUpdated();
        updatedBy = getCurrentUserRef();
    }

    private Key<User> toKey(Ref<User> ref) {
        return ref == null ? null : ref.getKey();
    }

    private Ref<User> getCurrentUserRef() {
        return UserAdapterGae.currentUserRef()
            .orElse(null);
    }

}
