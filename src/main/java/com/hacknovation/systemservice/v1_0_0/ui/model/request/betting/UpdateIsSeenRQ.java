package com.hacknovation.systemservice.v1_0_0.ui.model.request.betting;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Sombath
 * create at 9/4/23 2:40 PM
 */

@Data
public class UpdateIsSeenRQ {

    @NotNull
    @Min(0)
    private Long orderId;

    @NotNull
    @NotEmpty
    private String type;

    @NotNull
    @NotEmpty
    private String lottery;

}
