package com.hacknovation.systemservice.v1_0_0.ui.model.response.drawing;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * author : phokkinnky
 * date : 6/24/21
 */
@Data
public class DrawResultRS {
    private String type;
    private String date;
    private Date drawAt;
    private List<DrawItemsResultRS> items;
}
