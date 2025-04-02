package com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class EditTransferNumberRQ {

    @NotBlank(message = "Please provide lottery type")
    private String lotteryType;

    @NotEmpty(message = "Please provide a draw code")
    @NotBlank(message = "Please provide a draw code")
    private String drawCode;

    @NotEmpty(message = "Please provide a post code")
    @NotBlank(message = "Please provide a post code")
    private String postCode;

    @NotEmpty(message = "Please provide a rebate code")
    @NotBlank(message = "Please provide a rebate code")
    private String rebateCode;

    @NotEmpty(message = "Please provide a transfer numbers")
    @Size(min = 1)
    private List<@Valid TransferItemRQ> items;

}
