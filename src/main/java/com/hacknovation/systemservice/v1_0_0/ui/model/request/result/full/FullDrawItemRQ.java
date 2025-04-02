package com.hacknovation.systemservice.v1_0_0.ui.model.request.result.full;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;


@Data
public class FullDrawItemRQ {
    private BigInteger id;
    @NotNull
    private String rebateCode;

    private String number;
    @NotNull
    @Min(1)
    @Max(4)
    private Integer columnNumber;
}
