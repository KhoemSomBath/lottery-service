package com.hacknovation.systemservice.v1_0_0.ui.model.request.probability;

import com.hacknovation.systemservice.exception.anotation.Include;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/*
 * author: kangto
 * createdAt: 25/08/2022
 * time: 22:52
 */
@Data
public class UpdateProbabilityRQ {
    @Include(contains = "LEAP,VN2,TN", delimiter = ",")
    private String lotteryType;

    @NotEmpty
    @NotNull
    private String postCode;

    @NotEmpty
    @NotNull
    private String rebateCode;

    @NotEmpty
    @NotNull
    private String proKey;

    @NotNull
    @Min(0)
    private BigDecimal percentage;

    @NotNull
    private Boolean isProbability;

}
