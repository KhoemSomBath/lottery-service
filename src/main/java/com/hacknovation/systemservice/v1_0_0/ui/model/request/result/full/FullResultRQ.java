package com.hacknovation.systemservice.v1_0_0.ui.model.request.result.full;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class FullResultRQ {
    @NotNull
    private String lotteryType;
    @NotNull
    private String drawCode;

    @NotNull
    @NotEmpty
    private List<@Valid FullResultRowRQ> results = new ArrayList<>();
}
