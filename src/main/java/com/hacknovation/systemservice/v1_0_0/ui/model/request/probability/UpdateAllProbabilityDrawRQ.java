package com.hacknovation.systemservice.v1_0_0.ui.model.request.probability;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/*
 * author: kangto
 * createdAt: 14/10/2022
 * time: 13:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateAllProbabilityDrawRQ extends UpdateAllProbabilityRQ {
    @NotNull
    private String shiftCode;
}
