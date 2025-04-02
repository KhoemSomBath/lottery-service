package com.hacknovation.systemservice.v1_0_0.io.entity;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.common.BaseEntity;
import com.sun.istack.Nullable;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "user_has_lotteries")
public class UserHasLotteryEntity extends BaseEntity {

    @Column(name = "uc")
    private String userCode;

    @Nullable
    @Column(name = "lottery_code")
    private String lotteryCode;

    @Nullable
    @Column(name = "rc")
    private String rebateCode;

    @Nullable
    @Column(name = "rebate_rate")
    private BigDecimal rebateRate = BigDecimal.ZERO;

    @Nullable
    @Column(name = "water_rate")
    private BigDecimal waterRate = BigDecimal.ZERO;

    @Nullable
    @Column(name = "share")
    private BigDecimal share = BigDecimal.ZERO;

    @Nullable
    @Column(name = "commission")
    private BigDecimal commission = BigDecimal.ZERO;

    @Nullable
    @Column(name = "max_bet_first")
    private BigDecimal maxBetFirst;

    @Nullable
    @Column(name = "max_bet_second")
    private BigDecimal maxBetSecond;

    @Nullable
    @Column(name = "limit_digit")
    private BigDecimal limitDigit;

    @Column(name = "max_bet_second_min")
    private String maxBetSecondMin;

    @Nullable
    @Column(name = "max_bet_range")
    private BigDecimal maxBetRange;

    @Nullable
    @Column(name = "max_bet_item_range")
    private Integer maxBetItemRange;

    @Nullable
    @Column(name = "status")
    private Boolean status = true;

    @Nullable
    @Column(name = "updated_by")
    private String updatedBy;

    public BigDecimal getRebateRate() {
        if(this.rebateRate == null) {
            if (LotteryConstant.REBATE2D.equalsIgnoreCase(this.rebateCode))
                return LotteryConstant.REBATE_RATE_2D;
            if (LotteryConstant.REBATE3D.equalsIgnoreCase(this.rebateCode))
                return LotteryConstant.REBATE_RATE_3D;
            if (LotteryConstant.REBATE4D.equalsIgnoreCase(this.rebateCode))
                return LotteryConstant.REBATE_RATE_4D;
            return BigDecimal.ZERO;
        }

        return rebateRate;
    }

    public BigDecimal getWaterRate() {
        if(this.waterRate == null)
            return BigDecimal.ZERO;
        return waterRate;
    }

    public BigDecimal getShare() {
        if(this.share == null)
            return BigDecimal.ZERO;
        return share;
    }

    public BigDecimal getCommission() {
        if(this.commission == null)
            return BigDecimal.ZERO;
        return commission;
    }

    public BigDecimal getMaxBetFirst() {
        if(this.maxBetFirst == null)
            return BigDecimal.ZERO;
        return maxBetFirst;
    }

    public BigDecimal getMaxBetSecond() {
        if(this.maxBetSecond == null)
            return BigDecimal.ZERO;
        return maxBetSecond;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserHasLotteryEntity that = (UserHasLotteryEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return 974058289;
    }
}
