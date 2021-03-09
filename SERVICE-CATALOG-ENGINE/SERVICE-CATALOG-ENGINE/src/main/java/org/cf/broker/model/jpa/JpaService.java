package org.cf.broker.model.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service")
public class JpaService {

    @JsonSerialize
    @Id
    @Column(name = "id" ,length = 128)
    private String id;

    @ManyToOne
    @JoinColumn(name = "svc_broker_id", referencedColumnName = "id")
    @JsonIgnore
    private JpaServiceBroker serviceBroker;

    @JsonSerialize
    @JsonProperty("svc_broker_id")
    private String svcBrokerId(){
        return this.serviceBroker.getId();
    }

    @JsonSerialize
    @JsonProperty("svc_nm")
    @Column(name = "svc_nm" ,length = 255)
    private String svcNm;


    @JsonSerialize
    @JsonProperty("bind_at")
    @Column(name = "bind_at", columnDefinition = "TINYINT", length=1)
    private boolean bindable;

    @JsonSerialize
    @JsonProperty("use_at")
    @Column(name = "use_at", nullable = false, columnDefinition = "TINYINT", length=1)
    private int useAt;

    @Lob
    @JsonSerialize
    @JsonProperty("dc")
    @Column(name = "dc" , columnDefinition = "TEXT")
    private String dc;

    @Lob
    @JsonSerialize
    @JsonProperty("tag")
    @Column(name = "tag", columnDefinition = "TEXT")
    private String tag;

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
    @JoinColumn(name = "svc_id")
    @Builder.Default
    private List<JpaServicePlan> servicePlanList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "svc_id")
    @Builder.Default
    private List<JpaServiceInstn> serviceInstnsList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "svc_id")
    @Builder.Default
    private List<JpaServiceTemplateDetail> serviceTemplateDetails = new ArrayList<>();


    @PrePersist
    void preInsert() {
        this.creatDe = LocalDateTime.now();
    }
}
