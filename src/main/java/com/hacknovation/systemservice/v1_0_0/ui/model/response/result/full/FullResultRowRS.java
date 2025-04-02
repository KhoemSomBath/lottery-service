package com.hacknovation.systemservice.v1_0_0.ui.model.response.result.full;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sombath
 * create at 14/10/22 1:51 PM
 */

@Data
public class FullResultRowRS {
    private String postCode;
    private String postGroup;
    private String rebateCode;
    private List<FullDrawItemRS> items = new ArrayList<>();
}
