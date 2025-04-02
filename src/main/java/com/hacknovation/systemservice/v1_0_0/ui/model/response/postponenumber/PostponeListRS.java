package com.hacknovation.systemservice.v1_0_0.ui.model.response.postponenumber;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * author: kangto
 * createdAt: 21/02/2022
 * time: 23:57
 */
@Data
public class PostponeListRS {
    private String lotteryType;
    private String drawCode;
    private Date drawAt;
    private List<PostponeItemRS> items = new ArrayList<>();
    private List<PostponeItemRS> allMembers = new ArrayList<>();
    private List<PostponeItemRS> defaultItems = new ArrayList<>();
}
