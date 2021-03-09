package org.openpaas.paasta.portal.common.api.entity.cc;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "app_usage_events")
public class AppUsageEventCc {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "guid")
    private String guid;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "memory_in_mb_per_instance")
    private int memory_in_mb_per_instance;

    @Column(name = "state")
    private String state;

    @Column(name = "app_guid")
    private String app_guid;

    @Column(name = "app_name")
    private String app_name;

    @Column(name = "space_guid")
    private String space_guid;

    @Column(name = "space_name")
    private String space_name;

    @Column(name = "org_guid")
    private String org_guid;

    @Column(name = "buildpack_guid")
    private String buildpack_guid;

    @Column(name = "buildpack_name")
    private String buildpack_name;

    @Column(name = "parent_app_name")
    private String parent_app_name;

    @Column(name = "parent_app_guid")
    private String parent_app_guid;

    @Column(name = "process_type")
    private String process_type;

    @Column(name = "task_guid")
    private String task_guid;

    @Column(name = "task_name")
    private String task_name;

    @Column(name = "package_guid")
    private String package_guid;

    @Column(name = "previous_state")
    private String previous_state;

    @Column(name = "previous_package_state")
    private String previous_package_state;

    @Column(name = "previous_memory_in_mb_per_instance")
    private String previous_memory_in_mb_per_instance;

    @Column(name = "previous_instance_count")
    private String previous_instance_count;

}
