package com.hacknovation.systemservice.v3_0_0.servive.betting.respond;

import lombok.Data;

import java.util.List;

/**
 * author : phokkinnky
 * date : 6/25/21
 */
@Data
public class CardRS {
    private String id;
    private List<OrderTicketRS> cards;
}
