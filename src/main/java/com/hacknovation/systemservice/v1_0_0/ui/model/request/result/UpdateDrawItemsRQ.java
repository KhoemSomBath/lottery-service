package com.hacknovation.systemservice.v1_0_0.ui.model.request.result;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * author : phokkinnky
 * date : 8/6/21
 */
@Data
public class UpdateDrawItemsRQ {
    @NotNull(message = "Please provide lottery type")
    @NotEmpty(message = "Please provide lottery type")
    private String lotteryType;

    @NotNull(message = "Please provide draw code")
    @NotEmpty(message = "Please provide draw code")
    private String drawCode;

    private Boolean isNight = Boolean.FALSE;

    @NotEmpty(message = "Please provide results list")
    @Size(min = 1)
    private List<DrawItemRQ> results;

}
