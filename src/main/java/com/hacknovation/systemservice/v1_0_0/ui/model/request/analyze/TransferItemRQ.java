package com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class TransferItemRQ {

    @NotEmpty(message = "Please provide a number")
    @NotBlank(message = "Please provide a number")
    private String number;
    @NotNull(message = "Please provide a amount")
    @Min(1)
    @Max(1000000)
    private BigDecimal transferAmount;

}
