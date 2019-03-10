package com.content_load_sb.dbops;

import com.content_load_sb.helper.CustomerDateAndTimeDeserialize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by asd on 8.11.2017.
 */

@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OID", unique = true)
    private Long oid;
    @Version
    @Column(name = "LAST_UPDATED_VERSION")
    private Long lastUpdated;
    @Column(name = "CREAYED_DATE")
    @JsonIgnore
    @JsonDeserialize(using = CustomerDateAndTimeDeserialize.class)
    private Date createdDate;
    @Column(name = "UPDATED_DATE")
    @JsonIgnore
    @JsonDeserialize(using = CustomerDateAndTimeDeserialize.class)
    private Date updatedDate;


    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}