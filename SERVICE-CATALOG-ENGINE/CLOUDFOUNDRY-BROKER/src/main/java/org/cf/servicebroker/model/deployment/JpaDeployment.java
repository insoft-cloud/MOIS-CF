package org.cf.servicebroker.model.deployment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "deployments")
@AllArgsConstructor
@NoArgsConstructor
public class JpaDeployment {

    @JsonSerialize
    @Id
    @Column(name = "name", length = 128)
    private String name;

    @Lob
    @JsonSerialize
    @Column(name = "deployments_yml", columnDefinition = "TEXT")
    @JsonProperty("deployments_yml")
    private String deployments_yml;

}
