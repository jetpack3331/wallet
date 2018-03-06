package hut34.wallet.framework;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import hut34.wallet.util.DateTimeUtils;
import org.springframework.contrib.gae.search.IndexType;
import org.springframework.contrib.gae.search.SearchIndex;

import java.time.OffsetDateTime;

/**
 * Created and updated timestamps set. On creation updated and created are the same.
 * <p>
 */
public class BaseEntityCore {

    public static class Fields {
        public static final String updated = "updated";
        public static final String created = "created";
    }

    @Index
    @SearchIndex(type = IndexType.NUMBER)
    private OffsetDateTime created;

    @Index
    @SearchIndex(type = IndexType.NUMBER)
    private OffsetDateTime updated;

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

    public OffsetDateTime getUpdated() {
        return updated;
    }

    /**
     * Optional hook for child entities
     */
    protected void onCreated() {
    }

    /**
     * Optional hook for child entities
     */
    protected void onUpdated() {
    }

    @OnSave
    private void setAuditableFieldsOnSave() {
        if (!skipSettingAuditableFields) {
            updated = DateTimeUtils.now();
            onUpdated();

            if (created == null) {
                onCreated();
                created = updated;
            }
        }
    }

}
