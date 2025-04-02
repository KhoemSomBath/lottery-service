package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.ticket;

import com.hacknovation.systemservice.v1_0_0.ui.model.dto.order.OrderItemsDTO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/*
 * author: kangto
 * createdAt: 11/01/2022
 * time: 10:12
 */
@Data
public class OrderItemsRS {
    private BigInteger itemId;
    private Integer orderId;
    private String ticketNumber;
    private Integer pageNumber;
    private Integer columnNumber;
    private Integer sectionNumber;
    private String posts;
    private Integer multiDigit;
    private String betType;
    private String betTitle;
    private Boolean isOneDigit;
    private Boolean isTwoDigit;
    private Boolean isThreeDigit;
    private Boolean isFourDigit;
    private String numberFrom;
    private String numberTo;
    private String numberThree;
    private String numberFour;
    private String numberDetail;
    private Integer numberQuantity;
    private String rebateCode;
    private String currencyCode;
    private BigDecimal totalAmount;
    private BigDecimal rebateRate;
    private BigDecimal betAmount = BigDecimal.ZERO;
    private BigDecimal waterRate = BigDecimal.ZERO;
    private BigDecimal waterAmount = BigDecimal.ZERO;
    private BigDecimal winAmount = BigDecimal.ZERO;
    private BigInteger winQty;
    private BigDecimal winLoseAmount = BigDecimal.ZERO;
    private Integer status;
    private Integer pairStatus;

    public OrderItemsRS(OrderItemsDTO itemsDTO) {
        BeanUtils.copyProperties(itemsDTO, this);
        this.setItemId(itemsDTO.getId());
        this.setWaterRate(itemsDTO.getCommission());
        if (this.getWaterRate().compareTo(BigDecimal.ZERO) > 0)
            this.setWaterAmount(this.getTotalAmount().multiply(this.getWaterRate()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN));
        this.setWinLoseAmount(this.getWaterAmount().subtract(this.getWinAmount().multiply(this.getRebateRate())));
    }
}
