package com.hacknovation.systemservice.v3_0_0.servive.betting.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Sombath
 * create at 7/9/21 12:13 PM
 */

@Data
public class EditPostRQ {
    @NotEmpty(message = "Please provide a lottery type")
    @NotBlank(message = "Please provide a lottery type")
    private String lotteryType;
    @NotNull(message = "Please provide a card id")
    private String cardId;
    @NotBlank
    private String post;
}
