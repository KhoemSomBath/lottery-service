package com.hacknovation.systemservice.v1_0_0.ui.model.response.lockbetTemplate;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*
 * author: kangto
 * createdAt: 17/03/2022
 * time: 15:50
 */
@Data
public class LockbetListRS {
    private String shiftCode;
    private String drawAt;
    private Boolean isNight;
    private List<String> columns = new ArrayList<>();
    private List<LockbetItemRS> items = new ArrayList<>();
}
