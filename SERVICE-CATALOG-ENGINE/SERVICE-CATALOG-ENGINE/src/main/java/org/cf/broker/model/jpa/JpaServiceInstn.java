package org.cf.broker.model.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service_instn")
public class JpaServiceInstn {

    @JsonSerialize
    @Id
    @Column(name = "id", length = 128)
    private String id;

    @JsonSerialize
    @ManyToOne
    @JoinColumn(name = "svc_id", referencedColumnName = "id")
    @JsonProperty("svc_id")
    private JpaService service;

    @JsonSerialize
    @ManyToOne
    @JoinColumn(name = "svc_plan_id", referencedColumnName = "id")
    @JsonProperty("svc_plan_id")
    private JpaServicePlan servicePlan;

    @JsonSerialize
    @Column(name = "instn_nm")
    @JsonProperty("instn_nm")
    private String instnNm;

    @JsonSerialize
    @Column(name = "user_id" , length = 50)
    @JsonProperty("user_id")
    private String userId;

    @JsonSerialize
    @Column(name = "conect_info", columnDefinition = "TEXT")
    @JsonProperty("conect_info")
    private String conectInfo;

    @JsonSerialize
    @Column(name = "conect_url", length = 128)
    @JsonProperty("conect_url")
    private String conectUrl;

    @JsonSerialize
    @Column(name = "mesur_ty", length = 128)
    @JsonProperty("service_metering_type")
    private String mesurTy;


    @JsonSerialize
    @Column(name = "use_at", nullable = false, columnDefinition = "TEXT")
    @JsonProperty("use_at")
    private int useAt;

    @JsonSerialize
    @Column(name = "sttus_value", length = 15)
    @JsonProperty("sttus_value")
    private String sttusValue;

    @JsonSerialize
    @Column(name = "project_id", length = 128)
    @JsonProperty("project_id")
    private String projectId;

    @JsonSerialize
    @Column(name = "broker_code", length = 4)
    @JsonProperty("broker_code")
    private int brokerCode;

    @JsonSerialize
    @Column(name = "client_id", length = 200)
    @JsonProperty("client_id")
    private String clientId;

    @JsonSerialize
    @Column(name = "broker_value", columnDefinition = "TEXT")
    @JsonProperty("broker_value")
    private String brokerMsg;


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonProperty("creat_de")
    @Column(name = "creat_de", columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime creatDe;

    @JsonSerialize
    @JsonProperty("creat_id")
    @Column(name = "creat_id")
    private String creatId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonProperty("updt_de")
    @Column(name = "updt_de", columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updtDe;

    @JsonSerialize
    @JsonProperty("updt_id")
    @Column(name = "updt_id")
    private String updtId;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "svc_instn_id")
    @JsonIgnore
    @Builder.Default
    private List<JpaServiceUseInfo> serviceUseInfoList = new ArrayList<>();

    @PrePersist
    void preInsert() {
        this.creatDe = LocalDateTime.now();
    }
}
