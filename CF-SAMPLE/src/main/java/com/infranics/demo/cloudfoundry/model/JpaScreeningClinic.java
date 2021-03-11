package com.infranics.demo.cloudfoundry.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="SCREENING_CLINIC")
public class JpaScreeningClinic {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(length = 128)
    private String adtFrDd;

    @Column(length = 128)
    private String hospTyTpCd;

    @Column(length = 128)
    private String sgguNm;

    @Column(length = 128)
    private String sidoNm;

    @Column(length = 128)
    private String spclAdmTyCd;

    @Column(length = 128)
    private String telno;

    @Column(length = 128)
    private String yadmNm;

}
