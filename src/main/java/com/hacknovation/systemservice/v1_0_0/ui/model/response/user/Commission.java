package com.hacknovation.systemservice.v1_0_0.ui.model.response.user;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserHasLotteryEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.userlottery.UserHasLotteryTO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sombath
 * create at 24/11/21 4:10 PM
 */

@Data
public class Commission {
    private String userCode;
    private String rebateCode;
    private String lotteryCode;
    private BigDecimal share = BigDecimal.valueOf(100);
    private BigDecimal waterRate = BigDecimal.valueOf(100);
    private BigDecimal rebateRate = BigDecimal.ZERO;
    private BigDecimal commission = BigDecimal.ZERO;
    private BigDecimal maxBetFirst = BigDecimal.ZERO;
    private BigDecimal maxBetSecond = BigDecimal.ZERO;
    private BigDecimal limitDigit = BigDecimal.ZERO;
    private String maxBetSecondAt;
    private BigDecimal maxBetRange;
    private Integer maxBetItemRange;
    private List<ShiftMaxBetRS> shifts = new ArrayList<>();


    public Commission(UserHasLotteryEntity userHasLotteryEntity) {
        BeanUtils.copyProperties(userHasLotteryEntity, this);
        this.maxBetSecondAt = userHasLotteryEntity.getMaxBetSecondMin();
    }

    public Commission(UserHasLotteryTO userHasLotteryTO) {
        BeanUtils.copyProperties(userHasLotteryTO, this);
        this.lotteryCode = userHasLotteryTO.getOriginLotteryCode();
    }

    public Commission() {}

    public BigDecimal getRebateRate() {
        if (this.isDefault()) {
            switch (this.getRebateCode()) {
                case LotteryConstant.REBATE2D:
                    return LotteryConstant.REBATE_RATE_2D;
                case LotteryConstant.REBATE3D:
                    return LotteryConstant.REBATE_RATE_3D;
                case LotteryConstant.REBATE4D:
                    return LotteryConstant.REBATE_RATE_4D;
                default:
                    return LotteryConstant.REBATE_RATE_1D;
            }
        }
        return rebateRate;
    }

    public BigDecimal getCommission() {
        if (this.isDefault()) {
            switch (this.getRebateCode()) {
                case LotteryConstant.REBATE2D:
                    return LotteryConstant.COMMISSION_RATE_2D;
                case LotteryConstant.REBATE3D:
                    return LotteryConstant.COMMISSION_RATE_3D;
                case LotteryConstant.REBATE4D:
                    return LotteryConstant.COMMISSION_RATE_4D;
                default:
                    return LotteryConstant.COMMISSION_RATE_1D;
            }
        }
        return commission;
    }

    public BigDecimal getMaxBetFirst() {
        if (this.isDefault()) {
            switch (this.getRebateCode()) {
                case LotteryConstant.REBATE2D:
                    return LotteryConstant.MAX_BET_FIRST_2D;
                case LotteryConstant.REBATE3D:
                    return LotteryConstant.MAX_BET_FIRST_3D;
                case LotteryConstant.REBATE4D:
                    return LotteryConstant.MAX_BET_FIRST_4D;
                default:
                    return LotteryConstant.MAX_BET_FIRST_1D;
            }
        }
        return maxBetFirst;
    }

    public BigDecimal getMaxBetSecond() {
        if (this.isDefault()) {
            switch (this.getRebateCode()) {
                case LotteryConstant.REBATE2D:
                    return LotteryConstant.MAX_BET_SECOND_2D;
                case LotteryConstant.REBATE3D:
                    return LotteryConstant.MAX_BET_SECOND_3D;
                case LotteryConstant.REBATE4D:
                    return LotteryConstant.MAX_BET_FIRST_4D;
                default:
                    return LotteryConstant.MAX_BET_SECOND_1D;
            }
        }
        return maxBetSecond;
    }

    public BigDecimal getLimitDigit() {
        if (this.isDefault()) {
            switch (this.getRebateCode()) {
                case LotteryConstant.REBATE2D:
                    return LotteryConstant.LIMIT_DIGIT_2D;
                case LotteryConstant.REBATE3D:
                    return LotteryConstant.LIMIT_DIGIT_3D;
                case LotteryConstant.REBATE4D:
                    return LotteryConstant.LIMIT_DIGIT_4D;
                default:
                    return LotteryConstant.LIMIT_DIGIT_1D;
            }
        }

        return limitDigit;
    }

    public boolean isDefault() {
        return (this.userCode == null);
    }
}
