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
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service_broker")
public class JpaServiceBroker {

    @Id
    @Column(name = "id" ,length = 128)
    private String id;

    @NotNull
    @Column(name = "broker_url", length = 50)
    private String brokerUrl;

    @Column(name = "broker_ty", length = 3)
    private String brokerTy;

    @JsonSerialize
    @Column(name = "use_at", nullable = false, columnDefinition = "TINYINT", length=1)
    private int useAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "creat_de", columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime creatDe;

    @Column(name = "creat_id")
    private String creatId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "updt_de", columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updtDe;

    @JsonSerialize
    @Column(name = "updt_id")
    private String updtId;

    @OneToMany(mappedBy = "serviceBroker", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    @JsonSerialize
    private List<JpaService> serviceList = new ArrayList<>();

    @PrePersist
    void preInsert() {
        this.creatDe = LocalDateTime.now();
    }

}
