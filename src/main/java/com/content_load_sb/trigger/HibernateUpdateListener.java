package com.content_load_sb.trigger;

import com.content_load_sb.dbops.BaseEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class HibernateUpdateListener {
    @PrePersist
    @PreUpdate
    public void setUpdatedAt(BaseEntity entity) {
        entity.setUpdatedDate(new Date());
    }
}