package com.hacknovation.systemservice.v1_0_0.ui.model.dto;

import lombok.Data;

/*
 * author: kangto
 * createdAt: 01/02/2022
 * time: 14:47
 */
@Data
public class WinOrderItemsDTO {
    private String drawCode;
    private Integer orderId;
    private Integer orderItemId;
    private String winDetail;
    private Integer winQty;
    private Boolean isSettlement;
}
