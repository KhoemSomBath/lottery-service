package com.hacknovation.systemservice.v3_0_0.servive.betting.respond;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRS {
    private String id;
    private Long itemId;
    private Integer pageNumber;
    private Integer columnNumber;
    private Integer sectionNumber;
    private String betType;
    private String betTitle;
    private Integer multiDigit;
    private String numberFrom;
    private String numberTo;
    private String numberThree;
    private String numberFour;
    private Integer pairStatus;
    private Boolean isOneDigit;
    private Boolean isTwoDigit;
    private Boolean isThreeDigit;
    private Boolean isFourDigit;
    private String numberDetail;
    private Integer numberQuantity;
    private String currencyCode;
    private BigDecimal betAmount = BigDecimal.ZERO;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private BigDecimal waterRate = BigDecimal.ZERO;
    private BigDecimal waterAmount = BigDecimal.ZERO;
    private BigDecimal winAmount = BigDecimal.ZERO;
    private BigDecimal winLoseAmount = BigDecimal.ZERO;
    private String rebateCode;
    private Boolean isSettlement;
    private Boolean isWin;
    private Integer status;
    private Integer winQty = 0;

}
