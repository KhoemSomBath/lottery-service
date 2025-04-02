package com.hacknovation.systemservice.v1_0_0.ui.model.request.cancelticket;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Sombath
 * create at 1/3/23 3:16 PM
 */

@Data
public class CancelTicketRQ {

    @NotNull
    private Long id;

    @NotNull
    private String lotteryType;

    @NotNull
    private String drawCode;

    @Min(0)
    private Integer status = 0;

}
