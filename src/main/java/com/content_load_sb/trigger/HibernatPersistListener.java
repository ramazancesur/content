package com.content_load_sb.trigger;

import com.content_load_sb.dbops.BaseEntity;

import javax.persistence.PrePersist;
import java.util.Date;

public class HibernatPersistListener {
    @PrePersist
    public void setCreatedAt(BaseEntity entity) {
        entity.setCreatedDate(new Date());
        entity.setUpdatedDate(new Date());
    }
}
