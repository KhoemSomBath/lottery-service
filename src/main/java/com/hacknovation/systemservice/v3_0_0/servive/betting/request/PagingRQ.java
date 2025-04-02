package com.hacknovation.systemservice.v3_0_0.servive.betting.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class PagingRQ {
    @NotEmpty(message = "Please provide a draw code")
    @NotBlank(message = "Please provide a draw code")
    private String userCode;

    @NotEmpty(message = "Please provide a draw code")
    @NotBlank(message = "Please provide a draw code")
    private String drawCode;

    @NotEmpty(message = "Please provide a lottery code")
    @NotBlank(message = "Please provide a lottery code")
    private String lotteryCode;

    private Integer pageNumber;

}
