package com.content_load_sb.persistmysql.entity;

import com.content_load_sb.dbops.BaseEntity;
import com.content_load_sb.trigger.HibernatPersistListener;
import com.content_load_sb.trigger.HibernateUpdateListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Entity
@Table(name = "DATA_TRANSFER_INFO")
@EntityListeners({
        HibernateUpdateListener.class,
        HibernatPersistListener.class
})
public class DataTransferInfoMySql extends BaseEntity {
    @Column(name = "TOTAL_FILE_SIZE")
    private Long totalFileSize;
    @Column(name = "REMAINING_FILE_SIZE")
    private Long remainingFileSize;
    @Column(name = "PERCENT")
    private float percent;

    public Long getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(Long totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    public Long getRemainingFileSize() {
        return remainingFileSize;
    }

    public void setRemainingFileSize(Long remainingFileSize) {
        this.remainingFileSize = remainingFileSize;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
