package org.cf.broker.model.jpa;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "service_tmplat")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class JpaServiceTemplate {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize
    @Id
    @Column(name = "id", length = 128)
    private int id;

    @Column(name = "tmplat_nm", length = 128)
    private String tmplatNm;

    @Column(name = "tmplat_file", columnDefinition = "TEXT")
    private String tmplatFilm;

    @Column(name = "use_at", nullable = false, columnDefinition = "TINYINT", length=1)
    private int useAt;

    @Column(name = "dc", columnDefinition = "TEXT")
    private String dc;

    @Column(name = "tag", columnDefinition = "TEXT")
    private String tag;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "svc_tmplat_id")
    @Builder.Default
    private List<JpaServiceTemplateDetail> serviceTemplateDetails = new ArrayList<>();
}
