package com.content_load_sb.persistmysql.entity;

import com.content_load_sb.dbops.BaseEntity;
import com.content_load_sb.trigger.HibernatPersistListener;
import com.content_load_sb.trigger.HibernateUpdateListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by asd on 13.11.2017.
 */
@Entity
@Table(name = "DIRECTORIES")
@EntityListeners({
        HibernateUpdateListener.class,
        HibernatPersistListener.class
})
public class TargetDirectory extends BaseEntity {
    @Column(name = "PATH")
    private String path;
    @Column(name = "PATH_HEAD")
    private String pathHEAD;
    @Column(name = "PATH_TAIL")
    private String pathTAIL;
    @Column(name = "NAME")
    private String name;
    @Column(name = "SIZE")
    private String size;
    @Column(name = "PARENT_PATH_TAIL")
    private String parentPathTAIL;
    @Column(name = "INSERT_DATE_TO_KIFE")
    private LocalDateTime insertDateToKIFE;
    @Column(name = "UPDATE_DATE_TO_KIFE")
    private LocalDateTime updateDateAtKIFE;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathHEAD() {
        return pathHEAD;
    }

    public void setPathHEAD(String pathHEAD) {
        this.pathHEAD = pathHEAD;
    }

    public String getPathTAIL() {
        return pathTAIL;
    }

    public void setPathTAIL(String pathTAIL) {
        this.pathTAIL = pathTAIL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getParentPathTAIL() {
        return parentPathTAIL;
    }

    public void setParentPathTAIL(String parentPathTAIL) {
        this.parentPathTAIL = parentPathTAIL;
    }

    public LocalDateTime getInsertDateToKIFE() {
        return insertDateToKIFE;
    }

    public void setInsertDateToKIFE(LocalDateTime insertDateToKIFE) {
        this.insertDateToKIFE = insertDateToKIFE;
    }

    public LocalDateTime getUpdateDateAtKIFE() {
        return updateDateAtKIFE;
    }

    public void setUpdateDateAtKIFE(LocalDateTime updateDateAtKIFE) {
        this.updateDateAtKIFE = updateDateAtKIFE;
    }
}