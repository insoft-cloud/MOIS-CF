package org.cf.broker.model.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "service_use_info")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JpaServiceUseInfo {

    @JsonSerialize
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "svc_plan_id", referencedColumnName = "id")
    private JpaServicePlan jpaServicePlan;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "svc_instn_id", referencedColumnName = "id")
    private JpaServiceInstn jpaServiceInstance;

    @JsonSerialize
    @JsonProperty("svc_id")
    private String svcInstnId(){
        return jpaServiceInstance.getId();
    }

    @JsonSerialize
    @JsonProperty("svc_plan_id")
    private String svcPlanId(){
        return jpaServicePlan.getId();
    }

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "begin_de")
    @JsonProperty("begin_de")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime beginDe;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "end_de")
    @JsonProperty("end_de")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endDe;

    @JsonSerialize
    @Column(name = "rntfee")
    @JsonProperty("rntfee")
    private int rntfee;

}
