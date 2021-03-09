package org.cf.broker.model.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "service_image")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class JpaServiceImage {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize
    @Id
    @Column(name = "id", length = 128)
    private int serviceImageId;

    @JsonSerialize
    @Column(name = "image_nm", length = 128)
    private String imageNm;

    @JsonSerialize
    @Column(name = "svc_platform_ty", length = 128)
    private String platformType;

    @JsonSerialize
    @Column(name = "svc_ty", length = 128)
    private String serviceType;

    @JsonSerialize
    @Column(name = "image_ty", length = 128)
    private String imageTy;

    @JsonSerialize
    @Column(name = "dc", length = 128)
    private String dc;

    @JsonSerialize
    @Column(name = "use_at", length = 1)
    private int useAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "creat_de", columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonIgnore
    private LocalDateTime creatDe;

    @JsonSerialize
    @Column(name = "creat_id")
    @JsonIgnore
    private String creatId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "updt_de", columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonIgnore
    private LocalDateTime updtDe;

    @JsonSerialize
    @Column(name = "updt_id")
    @JsonIgnore
    private String updtId;

}
