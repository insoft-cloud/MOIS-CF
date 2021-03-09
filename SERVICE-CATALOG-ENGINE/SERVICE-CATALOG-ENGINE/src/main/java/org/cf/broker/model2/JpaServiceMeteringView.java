package org.cf.broker.model2;

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
@Table(name = "TOPS_SERVICE_VIEW")
public class JpaServiceMeteringView {

    @JsonSerialize
    @Column(name = "svc_nm", length = 50)
    private String svcNm;

    @JsonSerialize
    @Column(name = "dc", length = 200)
    private String dc;

    @JsonSerialize
    @Column(name = "creat_dt", length = 20)
    private BigInteger creatDt;

    @JsonSerialize
    @Column(name = "creat_user_id", length = 20)
    private String creatUserId;

    @JsonSerialize
    @Column(name = "upd_dt", length = 20)
    private BigInteger updDt;

    @JsonSerialize
    @Column(name = "upd_user_id", length = 20)
    private String updUserId;

    @JsonSerialize
    @Column(name = "prjct_id", length = 128)
    private String prjctId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize
    @Id
    @Column(name = "instn_id", length = 128)
    private String instnId;

    @JsonSerialize
    @Column(name = "svc_stt", length = 4)
    private String svcStt;

    @JsonSerialize
    @Column(name = "svc_id", length = 128)
    private String svcId;

    @JsonSerialize
    @Column(name = "end_dt", length = 20)
    private BigInteger endDe;

}
