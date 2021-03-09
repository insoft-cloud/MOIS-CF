package org.cf.broker.model.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
//@Table(name = "metering_info")
public class JpaServiceMeteringInfo {

    @Id
    @Column(name = "id")
    @JsonIgnore
    private String id;

    @Column(name = "svc_id")
    private String serviceId;

    @Column(name = "svc_instn_id")
    private String serviceInstanceId;

    @JsonSerialize
    @Column(name = "svc_plan_id", length = 128)
    private String planId;

    @JsonSerialize
    @Column(name = "unit_ty", length = 4)
    private String unitType;

    @JsonSerialize
    @Column(name = "unit_value", length = 50)
    private String unitValue;

    @JsonSerialize
    @Column(name = "use_time", length = 20)
    private String useTime;

    @JsonSerialize
    @Column(name = "prjct_id", length = 50)
    private String projectId;

    @JsonSerialize
    @Column(name = "creat_id", length = 255)
    private String creatId;

    @JsonSerialize
    @Column(name = "begin_date", length = 20)
    private String beginDate;

    @JsonSerialize
    @Column(name = " end_date", length = 20)
    private String  endDate;

    @JsonSerialize
    @Column(name = " mesur_ty", length = 3)
    private String  meteringType;

}
