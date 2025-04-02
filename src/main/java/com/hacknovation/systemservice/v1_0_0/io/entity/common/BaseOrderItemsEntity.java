package com.hacknovation.systemservice.v1_0_0.io.entity.common;

import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.util.Date;

/*
 * author: kangto
 * createdAt: 09/01/2022
 * time: 11:39
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public class BaseOrderItemsEntity extends BaseEntity{

    @Column(name = "oi")
    private Integer orderId;

    @Nullable
    @Column(name = "dc")
    private String drawCode;

    @Column(name = "draw_at")
    private Date drawAt;

    @Nullable
    @Column(name = "tn")
    private String ticketNumber;

    @Nullable
    @Column(name = "pn")
    private Integer pageNumber;

    @Nullable
    @Column(name = "cn")
    private Integer columnNumber;

    @Nullable
    @Column(name = "sn")
    private Integer sectionNumber;

    @Column(name = "posts")
    private String posts;

    @Column(name = "post_analyze")
    private String postAnalyze;

    @Nullable
    @Column(name = "is_lo")
    private Boolean isLo;

    @Nullable
    @Column(name = "multi_digit")
    private Integer multiDigit;

    @Nullable
    @Column(name = "bet_title")
    private String betTitle;

    @Nullable
    @Column(name = "bet_type")
    private String betType;

    @Nullable
    @Column(name = "rc")
    private String rebateCode;

    @Nullable
    @Column(name = "is_one_digit")
    private Boolean isOneDigit = Boolean.FALSE;

    @Nullable
    @Column(name = "is_two_digit")
    private Boolean isTwoDigit = Boolean.FALSE;

    @Nullable
    @Column(name = "is_three_digit")
    private Boolean isThreeDigit = Boolean.FALSE;

    @Nullable
    @Column(name = "is_four_digit")
    private Boolean isFourDigit = Boolean.FALSE;

    @Nullable
    @Column(name = "number_from")
    private String numberFrom;

    @Nullable
    @Column(name = "number_to")
    private String numberTo;

    @Nullable
    @Column(name = "number_three")
    private String numberThree;

    @Nullable
    @Column(name = "number_four")
    private String numberFour;

    @Nullable
    @Column(name = "number_detail")
    private String numberDetail;

    @Nullable
    @Column(name = "number_quantity")
    private Integer numberQuantity;

    @Nullable
    @Column(name = "cc")
    private String currencyCode;

    @Nullable
    @Column(name = "bet_amount")
    private BigDecimal betAmount = BigDecimal.ZERO;

    @Nullable
    @Column(name = "total_amount")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Nullable
    @Column(name = "status")
    private Integer status;

    @Nullable
    @Column(name = "pair_status")
    private Integer pairStatus = 0;

    @Nullable
    @Column(name = "mc")
    private String memberCode;

    @Nullable
    @Column(name = "member_share")
    private BigDecimal share = BigDecimal.ZERO;

    @Nullable
    @Column(name = "member_commission")
    private BigDecimal commission = BigDecimal.ZERO;

    @Nullable
    @Column(name = "member_water_rate")
    private BigDecimal waterRate = BigDecimal.ZERO;

    @Nullable
    @Column(name = "member_rebate_rate")
    private BigDecimal rebateRate = BigDecimal.ZERO;

    @Nullable
    @Column(name = "agent_commission")
    private BigDecimal agentCommission = BigDecimal.ZERO;

    @Nullable
    @Column(name = "agent_rebate_rate")
    private BigDecimal agentRebateRate = BigDecimal.ZERO;

    @Nullable
    @Column(name = "master_commission")
    private BigDecimal masterCommission = BigDecimal.ZERO;

    @Nullable
    @Column(name = "master_rebate_rate")
    private BigDecimal masterRebateRate = BigDecimal.ZERO;

    @Nullable
    @Column(name = "senior_commission")
    private BigDecimal seniorCommission = BigDecimal.ZERO;

    @Nullable
    @Column(name = "senior_rebate_rate")
    private BigDecimal seniorRebateRate = BigDecimal.ZERO;

    @Nullable
    @Column(name = "super_commission")
    private BigDecimal superSeniorCommission = BigDecimal.ZERO;

    @Nullable
    @Column(name = "super_rebate_rate")
    private BigDecimal superSeniorRebateRate = BigDecimal.ZERO;

}
