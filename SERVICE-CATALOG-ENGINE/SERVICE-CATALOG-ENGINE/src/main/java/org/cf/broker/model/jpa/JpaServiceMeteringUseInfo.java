package org.cf.broker.model.jpa;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Table(name = "metering_use_info")
public class JpaServiceMeteringUseInfo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize
    @Id
    @Column(name = "id", length = 128)
    private int meteringId;

    @JsonSerialize
    @Column(name = "prjct_id", length = 128)
    private String projectId;

    @JsonSerialize
    @Column(name = "svc_id", length = 128)
    private String serviceId;

    @JsonSerialize
    @Column(name = "svc_instn_id", length = 128)
    private String serviceInstanceId;

    @JsonSerialize
    @Column(name = "svc_plan_id", length = 128)
    private String planId;

    @JsonSerialize
    @Column(name = "mesur_at", length = 1)
    private int mesurAt;

    @JsonSerialize
    @Column(name = "svc_sttus", length = 4)
    private String svcSttus;

    @JsonSerialize
    @Column(name = "unit_ty", length = 4)
    private String unitType;

    @JsonSerialize
    @Column(name = "unit_value", length = 20)
    private BigInteger unitValue;

    @JsonSerialize
    @Column(name = "begin_de", length = 30)
    private String beginDe;

    @JsonSerialize
    @Column(name = "end_de", length = 30)
    private String endDe;


}
