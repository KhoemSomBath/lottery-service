package com.hacknovation.systemservice.v1_0_0.ui.model.request.probability;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/*
 * author: kangto
 * createdAt: 25/08/2022
 * time: 23:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateProbabilityDrawRQ extends UpdateProbabilityRQ{
    @NotNull
    private String shiftCode;
}
