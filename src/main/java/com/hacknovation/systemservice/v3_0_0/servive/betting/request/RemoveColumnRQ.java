package com.hacknovation.systemservice.v3_0_0.servive.betting.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RemoveColumnRQ {

    @NotEmpty(message = "Please provide a userCode")
    @NotBlank(message = "Please provide a userCode")
    private String userCode;

    @NotEmpty(message = "Please provide a lottery type")
    @NotBlank(message = "Please provide a lottery type")
    private String lotteryType;

    @NotEmpty(message = "Please provide a draw code")
    @NotBlank(message = "Please provide a draw code")
    @Size(min = 9, max = 9)
    private String drawCode;

    @NotNull(message = "Please provide a page number")
    private Integer PageNumber;

    @NotNull(message = "Please provide a column number")
    private Integer columnNumber;

}
