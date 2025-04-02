package com.hacknovation.systemservice.v1_0_0.ui.model.response.lockbetTemplate;

import lombok.Data;

/*
 * author: kangto
 * createdAt: 17/03/2022
 * time: 15:47
 */
@Data
public class LockbetItemRS {
    private Integer itemId;
    private String dayOfWeek;
    private String stopLoAt;
    private String stopAAt;
    private String stopPostAt;
    private String stopDeleteAt;
}
