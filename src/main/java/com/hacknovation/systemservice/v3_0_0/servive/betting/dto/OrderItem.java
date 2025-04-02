package com.hacknovation.systemservice.v3_0_0.servive.betting.dto;

import java.math.BigDecimal;

/**
 * @author Sombath
 * create at 13/6/22 11:50 PM
 */

public interface OrderItem {

    Long getId();
    Long getOrderId();
    Integer getPageNumber();
    Integer getColumnNumber();
    Integer getSectionNumber();
    String getPost();
    Integer getMultiDigit();
    String getBetType();
    String getBetTitle();
    String getNumberFrom();
    String getNumberDetail();
    Integer getNumberQty();
    BigDecimal getBetAmount();
    BigDecimal getTotalAmount();
    BigDecimal getWaterRate();
    Boolean getIsLo();
    String getRebateCode();
    String getCurrencyCode();

}
