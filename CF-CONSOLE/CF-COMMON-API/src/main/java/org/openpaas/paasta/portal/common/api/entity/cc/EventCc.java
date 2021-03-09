package org.openpaas.paasta.portal.common.api.entity.cc;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "events")
public class EventCc {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "guid")
    private String guid;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(name = "type")
    private String type;
    @Column(name = "actor")
    private String actor;
    @Column(name = "actor_type")
    private String actor_type;
    @Column(name = "actee")
    private String actee;
    @Column(name = "actee_type")
    private String actee_type;
    @Column(name = "organization_guid")
    private String organization_guid;
    @Column(name = "metadata")
    private String metadata;
    @Column(name = "space_guid")
    private String space_guid;
    @Column(name = "actor_name")
    private String actor_name;
    @Column(name = "actee_name")
    private String actee_name;
    @Column(name = "actor_username")
    private String actor_username;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getActor_type() {
        return actor_type;
    }

    public void setActor_type(String actor_type) {
        this.actor_type = actor_type;
    }

    public String getActee() {
        return actee;
    }

    public void setActee(String actee) {
        this.actee = actee;
    }

    public String getActee_type() {
        return actee_type;
    }

    public void setActee_type(String actee_type) {
        this.actee_type = actee_type;
    }

    public String getOrganization_guid() {
        return organization_guid;
    }

    public void setOrganization_guid(String organization_guid) {
        this.organization_guid = organization_guid;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getSpace_guid() {
        return space_guid;
    }

    public void setSpace_guid(String space_guid) {
        this.space_guid = space_guid;
    }

    public String getActor_name() {
        return actor_name;
    }

    public void setActor_name(String actor_name) {
        this.actor_name = actor_name;
    }

    public String getActee_name() {
        return actee_name;
    }

    public void setActee_name(String actee_name) {
        this.actee_name = actee_name;
    }

    public String getActor_username() {
        return actor_username;
    }

    public void setActor_username(String actor_username) {
        this.actor_username = actor_username;
    }

    @Override
    public String toString() {
        return "EventCc{" +
                "id=" + id +
                ", guid='" + guid + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", actor='" + actor + '\'' +
                ", actor_type='" + actor_type + '\'' +
                ", actee='" + actee + '\'' +
                ", actee_type='" + actee_type + '\'' +
                ", organization_guid='" + organization_guid + '\'' +
                ", metadata='" + metadata + '\'' +
                ", space_guid='" + space_guid + '\'' +
                ", actor_name='" + actor_name + '\'' +
                ", actee_name='" + actee_name + '\'' +
                ", actor_username='" + actor_username + '\'' +
                '}';
    }
}
