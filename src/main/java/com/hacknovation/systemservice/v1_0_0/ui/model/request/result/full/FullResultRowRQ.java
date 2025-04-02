package com.hacknovation.systemservice.v1_0_0.ui.model.request.result.full;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sombath
 * create at 14/10/22 1:51 PM
 */

@Data
public class FullResultRowRQ {
    @NotNull
    private String postCode;
    @NotNull
    private String postGroup;
    @NotNull
    private String rebateCode;
    @NotEmpty
    private List<@Valid FullDrawItemRQ> items = new ArrayList<>();
}
