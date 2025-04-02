package com.hacknovation.systemservice.v3_0_0.servive.betting.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RemoveBettingItemRQ {

    @NotEmpty(message = "Please provide a lottery type")
    @NotBlank(message = "Please provide a lottery type")
    private String lotteryType;

    @NotEmpty(message = "Please provide a draw code")
    @NotBlank(message = "Please provide a draw code")
    @Size(min = 9, max = 9)
    private String drawCode;

    @NotNull(message = "Please provide a remove item id")
    private Integer itemId;

}
