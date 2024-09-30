package in.mannvender.splitwise.models;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.Date;

public class AuditListener {
    @PrePersist
    public void setCreatedAtAndUpdatedAt(BaseModel entity) {
        Date now = new Date();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
    }

    @PreUpdate
    public void setUpdatedAt(BaseModel entity) {
        entity.setUpdatedAt(new Date());
    }
}
