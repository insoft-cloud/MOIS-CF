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

@Getter
@Setter
@Entity
@Table(name = "service_tmplat_detail")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JpaServiceTemplateDetail {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize
    @Id
    @Column(name = "id", length = 128)
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "svc_id", referencedColumnName = "id")
    private JpaService jpaService;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "svc_tmplat_id", referencedColumnName = "id")
    private JpaServiceTemplate jpaServiceTemplate;

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

}
