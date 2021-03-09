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
import javax.persistence.Table;
import java.time.LocalDateTime;



@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Table(name = "metering_request_type")
public class JpaServiceMeteringRequestType {

    @JsonSerialize
    @Id
    @Column(name = "unit_ty", length = 4)
    private String unitType;

    @JsonSerialize
    @Column(name = "unit_info", length = 20)
    private String unitInfo;

    @JsonSerialize
    @Column(name = "unit_nm", length = 255)
    private String unitNm;

    @JsonSerialize
    @Column(name = "dc", columnDefinition = "TEXT")
    private String description;

    @JsonIgnore
    @Column(name = "use_at", length = 1)
    private int useAt;

    @JsonIgnore
    @Column(name = "creat_de", columnDefinition = "TIMESTAMP")
    private LocalDateTime creatDe;

    @JsonIgnore
    @Column(name = "creat_id", length = 50)
    private String creatId;

    @JsonIgnore
    @Column(name = "updt_de", columnDefinition = "TIMESTAMP")
    private LocalDateTime updtDe;

    @JsonIgnore
    @Column(name = "updt_id", length = 50)
    private String updtId;
}
