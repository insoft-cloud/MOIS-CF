package org.cf.broker.model3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(JpaVwLngPK.class)
@Table(name = "vw_lng")
public class JpaVwLng implements Serializable {

    @Column(name = "svc_id", length = 200)
    private String svcId;

    @Column(name = "svc_nm", length = 60)
    private String svcNm;

    @Column(name = "http_stat_cd", length = 10)
    private String httpStatCd;

    @Column(name = "app_nm", length = 100)
    private String appNm;

    @Column(name = "call_success", length = 1)
    private String callSuccess;

    @Id
    @Column(name = "crt_dt", length = 24)
    private String crtDt;

    @Column(name = "res_tm", length = 11)
    private int resTm;

    @Column(name = "data_sz_res", length = 15)
    private int dataSzRes;

    @Column(name = "data_sz", length = 15)
    private int dataSz;

    @Column(name = "crt_prjt_id", length = 128)
    private String crtPrjtId;

    @Column(name = "api_uri", length = 128)
    private String apiUri;

    @Id
    @Column(name = "clnt_id", length = 128)
    private String clntId;
}
