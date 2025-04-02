package com.hacknovation.systemservice.v1_0_0.ui.model.cache;

import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderItemsDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/*
 * author: kangto
 * createdAt: 02/03/2023
 * time: 15:15
 */
@Data
public class TicketBettingHZ {
    private Long orderId;
    private String key;
    private Date drawAt;
    private String lotteryType;
    private String memberCode;
    private String username;
    private String superSeniorCode;
    private List<OrderItemsDTO> items;
}
