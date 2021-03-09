package org.cf.broker.model.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "batch_log")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JpaServiceBatch {

    @JsonSerialize
    @Id
    @Column(name = "id", length = 128)
    private int id;

    @JsonSerialize
    @Column(name = "batch_name", length = 128)
    private String batchName;

    @JsonSerialize
    @Column(name = "suces_stts", length = 1)
    private String sucesStts;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonProperty("begin_de")
    @Column(name = "begin_de", columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime beginDe;


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonProperty("end_de")
    @Column(name = "end_de", columnDefinition = "TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endDe;
}
