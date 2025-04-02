package com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse;

import lombok.Data;

/*
 * author: kangto
 * createdAt: 23/12/2022
 * time: 17:55
 */
@Data
public class PostSelectRS {

    private String postCode;
    private Boolean isHighlight = false;

}
