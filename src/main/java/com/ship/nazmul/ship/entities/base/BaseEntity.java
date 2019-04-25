package com.ship.nazmul.ship.entities.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ship.nazmul.ship.commons.utils.DateUtil;
import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.User;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @OneToOne
    private User createdBy;

    @OneToOne
    private User updatedBy;

    @PrePersist
    private void onBasePersist() {
        this.created = new Date();
        this.lastUpdated = new Date();
        this.createdBy = getCurrentUser();
    }

    @PreUpdate
    private void onBaseUpdate() {
        this.lastUpdated = new Date();
        this.updatedBy = getCurrentUser();
    }

    @JsonIgnore
    public User getCurrentUser() {
        return SecurityConfig.getCurrentUser();
    }

    public String getReadableDate(Date date) {
        return DateUtil.getReadableDate(date);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Date getCreated() {
        return created;
    }


    public Date getLastUpdated() {
        return lastUpdated;
    }


    @JsonIgnore
    public User getCreatedBy() {
        return createdBy;
    }

    @JsonIgnore
    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", created=" + created +
                ", lastUpdated=" + lastUpdated +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                '}';
    }
}
