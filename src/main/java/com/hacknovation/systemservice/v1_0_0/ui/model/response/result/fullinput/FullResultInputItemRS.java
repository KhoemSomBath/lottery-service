package com.hacknovation.systemservice.v1_0_0.ui.model.response.result.fullinput;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigInteger;

@Data
public class FullResultInputItemRS {
    private BigInteger id;

    @JsonIgnore
    private String twoDigits;
    @JsonIgnore
    private String threeDigits;
    @JsonIgnore
    private String fourDigits;
    @JsonIgnore
    private String fiveDigits;
    @JsonIgnore
    private String sixDigits;

    private String rebateCode;
    private String number;
    private Integer columnNumber;
    private String lotteryType;
    private String postCode;
    private Boolean isStartRender = Boolean.FALSE;
}
