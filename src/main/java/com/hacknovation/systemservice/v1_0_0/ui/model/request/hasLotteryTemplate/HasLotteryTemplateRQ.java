package com.hacknovation.systemservice.v1_0_0.ui.model.request.hasLotteryTemplate;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;


@Data
public class HasLotteryTemplateRQ {

    @NotEmpty
    private String dayOfWeek;

    @NotEmpty
    private String lotteryType;

    @NotEmpty
    private String userCode;

    @NotEmpty
    private String postCode;

    @NotEmpty
    private String rebateCode;

    private Boolean status = true;
    private BigDecimal limitDigit = BigDecimal.ZERO;

}
