package org.cf.broker.model.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Table(name = "metering_info")
public class JpaServiceMetering {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize
    @Id
    @Column(name = "id", length = 128)
    private int meteringId;

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
    @Column(name = "mesur_ty", length = 3)
    private String mesurTy;

    @JsonSerialize
    @Column(name = "begin_de", length = 20)
    private String beginDe;

    @JsonSerialize
    @Column(name = "end_de", length = 20)
    private String endDe;

    @JsonSerialize
    @Column(name = "unit_ty", length = 4)
    private String unitType;

    @JsonSerialize
    @Column(name = "unit_value", length = 20)
    private BigInteger unitValue;

    @JsonSerialize
    @Column(name = "prjct_id", length = 50)
    private String projectId;

    @JsonSerialize
    @Column(name = "use_at", length = 1)
    private int useAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "creat_de", columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime creatDe;

    @JsonSerialize
    @Column(name = "creat_id", length = 50)
    private String creatId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "updt_de", columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updtDe;

    @JsonSerialize
    @Column(name = "updt_id", length = 50)
    private String updtId;


}
