package com.hacknovation.systemservice.v1_0_0.ui.model.response.probability;

import com.hacknovation.systemservice.v1_0_0.io.entity.ProbabilityEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

/*
 * author: kangto
 * createdAt: 25/08/2022
 * time: 23:19
 */
@Data
public class ProbabilityItemRS {
    private String lotteryType;
    private String postCode;
    private String rebateCode;
    private String proKey;
    private Boolean isProbability;
    private BigDecimal percentage;
    private String updatedBy;
    private Date updatedAt;

    public ProbabilityItemRS(ProbabilityEntity item) {
        BeanUtils.copyProperties(item, this);
        proKey = item.getProbabilityKey();
    }

    public ProbabilityItemRS() {
    }
}
