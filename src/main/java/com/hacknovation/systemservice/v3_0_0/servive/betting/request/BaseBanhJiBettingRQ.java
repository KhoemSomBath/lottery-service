package com.hacknovation.systemservice.v3_0_0.servive.betting.request;

import com.sun.istack.Nullable;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class BaseBanhJiBettingRQ {
    @NotEmpty(message = "Please provide a draw code")
    @NotBlank(message = "Please provide a draw code")
    @Size(min = 9, max = 9)
    private String drawCode;

    @NotEmpty(message = "Please provide a user code")
    @NotBlank(message = "Please provide a user code")
    private String userCode;

    private String lotteryType;

    @Nullable
    private Integer pageNumber;

    @Nullable
    private Integer columnNumber;

    @NotEmpty(message = "Please provide a betting list")
    private List<@Valid BettingRQ> betting;
}
