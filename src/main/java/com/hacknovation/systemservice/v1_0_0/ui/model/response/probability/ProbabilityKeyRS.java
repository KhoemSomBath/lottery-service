package com.hacknovation.systemservice.v1_0_0.ui.model.response.probability;

import lombok.Data;

/*
 * author: kangto
 * createdAt: 25/08/2022
 * time: 23:16
 */
@Data
public class ProbabilityKeyRS {

    private String code;
    private String probabilityKey;

    public ProbabilityKeyRS(String code, String key) {
        this.code = code;
        this.probabilityKey = key;
    }
}
