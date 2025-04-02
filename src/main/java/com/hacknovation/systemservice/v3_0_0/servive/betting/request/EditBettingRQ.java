package com.hacknovation.systemservice.v3_0_0.servive.betting.request;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class EditBettingRQ {

    @NotEmpty(message = "Please provide a lottery type")
    @NotBlank(message = "Please provide a lottery type")
    private String lotteryType;

    @NotEmpty(message = "Please provide a currency code")
    @NotBlank(message = "Please provide a currency code")
    private String currencyCode;

    @NotEmpty(message = "Please provide a posts")
    @NotBlank(message = "Please provide a posts")
    private String posts;

    private Boolean isLo;

    @NotEmpty(message = "Please provide a bet type")
    @NotBlank(message = "Please provide a bet type")
    private String betType;

    private Boolean isOneDigit;
    private Boolean isTwoDigit;
    private Boolean isThreeDigit;
    private Boolean isFourDigit;

    @Min(2)
    @Max(4)
    private Integer multiDigit;

    @NotEmpty(message = "Please provide a number")
    @NotBlank(message = "Please provide a number")
    private String numberFrom;

    private String numberTo;
    private String numberThree;
    private String numberFour;

    @NotNull(message = "Please provide a amount")
    private BigDecimal betAmount;

    @NotNull(message = "Please provide a total amount")
    private BigDecimal totalAmount;

    @NotBlank(message = "Please provide a number detail")
    private String numberDetail;

    private String betTitle;
    private Integer sectionNumber;

    public String getPosts() {
        return posts.replaceAll("Lo", "");
    }
}
