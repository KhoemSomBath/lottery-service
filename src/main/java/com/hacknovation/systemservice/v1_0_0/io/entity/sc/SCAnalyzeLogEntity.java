package com.hacknovation.systemservice.v1_0_0.io.entity.sc;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;


@Entity
@Table(name = "sc_analyzing_log")
@Data
public class SCAnalyzeLogEntity {

    @Id
    private Long id;

    @Column(name = "dc")
    private String drawCode;

    @Column(name = "pc")
    private String postCode;

    @Column(name = "rc")
    private String rebateCode;

    @Column(name = "nu")
    private String number;

    @Column(name = "pro_key")
    private String proKey;

    @Column(name = "is_pro")
    private Boolean isPro;

    @Column(name = "ba")
    private BigDecimal betAmount;

    @Column(name = "ca")
    private BigDecimal commission;

    @Column(name = "prize")
    private BigDecimal prize;

    @Column(name = "ts")
    private BigDecimal totalSale;

    @Column(name = "tca")
    private BigDecimal totalCommission;

    @Column(name = "rp")
    private BigDecimal realPercentage;

    @Column(name = "pp")
    private BigDecimal protestPercentage;
}
