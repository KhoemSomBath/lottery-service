package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.ticket;

import com.hacknovation.systemservice.v1_0_0.ui.model.response.drawing.DrawRS;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*
 * author: kangto
 * createdAt: 11/01/2022
 * time: 11:18
 */
@Data
public class TicketListRS {
    private List<DrawRS> drawShifts = new ArrayList<>();
    private List<TicketRS> tickets = new ArrayList<>();
    private SummaryAmountRS summaryAmount;

}
