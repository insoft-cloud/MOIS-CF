package org.cf.servicebroker.model.plan;

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
import org.cf.servicebroker.model.service.JpaService;
import org.cf.servicebroker.model.serviceinstance.JpaServiceInstn;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "service_plan")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JpaServicePlan {
    @JsonSerialize
    @Id
    @Column(name = "id", length = 128)
    private String id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "svc_id", referencedColumnName = "id")
    private JpaService jpaService;

    @JsonSerialize
    @Column(name = "plan_nm")
    @JsonProperty("plan_nm")
    private String planNm;

    @JsonSerialize
    @Column(name = "rntfee")
    @JsonProperty("rntfee")
    private int rntfee;

    @JsonSerialize
    @Column(name = "ct_unit", length = 100)
    @JsonProperty("ct_unit")
    private String ctUnit;

    @JsonSerialize
    @Column(name = "detail_info", length = 300)
    @JsonProperty("detail_info")
    private String detailInfo;

    @JsonSerialize
    @Column(name = "use_at", nullable = false, columnDefinition = "TINYINT", length=1)
    @JsonProperty("use_at")
    private int useAt;

    @JsonSerialize
    @Column(name = "free_at",columnDefinition = "TINYINT", length=1)
    @JsonProperty("free_at")
    private int freeAt;

    @JsonSerialize
    @Column(name = "mntnc_ver", length = 50)
    @JsonProperty("mntnc_ver")
    private String mntncVer;

    @JsonSerialize
    @Column(name = "cntnc_disk")
    @JsonProperty("cntnc_disk")
    private String cntncDisk;

    @JsonSerialize
    @Column(name = "virtl_mchn_ty")
    @JsonProperty("virtl_mchn_ty")
    private String virtlMchnTy;

    @Lob
    @JsonSerialize
    @Column(name = "dc", columnDefinition = "TEXT")
    @JsonProperty("dc")
    private String dc;

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
    @JoinColumn(name = "svc_plan_id")
    @Builder.Default
    private List<JpaServiceInstn> serviceInstanceList = new ArrayList<>();

    @PrePersist
    void preInsert() {
        this.creatDe = LocalDateTime.now();
    }


    private Object[] getCost() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map cost = new HashMap();

        Map a = new HashMap();
        a.put("usd", this.rntfee);

        cost.put("amount", a);
        cost.put("unit", this.ctUnit);

        List<Map> b = new ArrayList();
        b.add(cost);

        return b.toArray();
    }


}
