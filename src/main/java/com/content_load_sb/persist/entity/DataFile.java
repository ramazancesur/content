package com.content_load_sb.persist.entity;

import com.content_load_sb.dbops.BaseEntity;
import com.content_load_sb.trigger.HibernatPersistListener;
import com.content_load_sb.trigger.HibernateUpdateListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DATA_FILE")
@EntityListeners({
        HibernateUpdateListener.class,
        HibernatPersistListener.class
})
public class DataFile extends BaseEntity {
    @Column(name = "PATH")
    private String path;
    @Column(name = "PATH_HEAD")
    private String pathHEAD;
    @Column(name = "PATH_TAIL")
    private String pathTAIL;
    @Column(name = "PARENT_PATH")
    private String parentPath;
    @Column(name = "PARENT_PATH_HEAD")

    private String parentPathHEAD;
    @Column(name = "PARENT_PATH_TAIL")
    private String parentPathTAIL;
    @Column(name = "NAME")
    private String name;
    @Column(name = "HASH")
    private String hash;
    @Column(name = "SIZE")
    private String size;
    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name = "INSERT_DATE_TO_KIFE")
    private LocalDateTime insertDateToKIFE;
    @Column(name = "UPDATE_DATE_TO_KIFE")
    private LocalDateTime updateDateAtKIFE;

    @ManyToOne
    @JoinColumn(name = "DIRECTORY_OID")
    private Directory directory;

    public DataFile() {
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

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getParentPathHEAD() {
        return parentPathHEAD;
    }

    public void setParentPathHEAD(String parentPathHEAD) {
        this.parentPathHEAD = parentPathHEAD;
    }

    public String getParentPathTAIL() {
        return parentPathTAIL;
    }

    public void setParentPathTAIL(String parentPathTAIL) {
        this.parentPathTAIL = parentPathTAIL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    @Override
    public String toString() {
        return "' ' ' ' '"
                + "\nid : " + this.getOid()
                + "\npath : " + this.path
                + "\npathHEAD : " + this.pathHEAD
                + "\npathTAIL : " + this.pathTAIL
                + "\nparentPath : " + this.parentPath
                + "\nparentPathHEAD : " + this.parentPathHEAD
                + "\nparentPathTAIL : " + this.parentPathTAIL
                + "\nname : " + this.name
                + "\nhash : " + this.hash
                + "\nsize : " + this.size
                + "\nfileType : " + this.fileType
                + "\ntransferDateToKIFE : " + this.insertDateToKIFE
                + "\nupdateDateAtKIFE : " + this.updateDateAtKIFE;
    }

}
