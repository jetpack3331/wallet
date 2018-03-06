package hut34.wallet.framework;

import java.time.OffsetDateTime;

public abstract class BaseDto {
    private OffsetDateTime created;
    private OffsetDateTime updated;

    public static <D extends BaseDto> D fromEntity(D dto, BaseEntityCore entity) {
        dto.setCreated(entity.getCreated());
        dto.setUpdated(entity.getUpdated());
        return dto;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    public OffsetDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(OffsetDateTime updated) {
        this.updated = updated;
    }
}
