package com.hacknovation.systemservice.v1_0_0.ui.model.dto.order;

import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempOrderItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempOrderItemsEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
public class OrderItemsDTO {

    private BigInteger id;
    private Integer orderId;
    private String drawCode;
    private Date drawAt;
    private String ticketNumber;
    private Integer pageNumber;
    private Integer columnNumber;
    private Integer sectionNumber;
    private String posts;
    private String postAnalyze;
    private Boolean isLo;
    private Integer multiDigit;
    private String betType;
    private String betTitle;
    private String rebateCode;
    private Boolean isOneDigit = Boolean.FALSE;
    private Boolean isTwoDigit = Boolean.FALSE;
    private Boolean isThreeDigit = Boolean.FALSE;
    private Boolean isFourDigit = Boolean.FALSE;
    private String numberFrom;
    private String numberTo;
    private String numberThree;
    private String numberFour;
    private String numberDetail;
    private Integer numberQuantity;
    private String currencyCode;
    private BigDecimal betAmount = BigDecimal.ZERO;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private String memberCode;
    private BigDecimal share = BigDecimal.ZERO;
    private BigDecimal commission = BigDecimal.ZERO;
    private BigDecimal waterRate = BigDecimal.ZERO;
    private BigDecimal rebateRate = BigDecimal.ZERO;
    private BigDecimal winAmount = BigDecimal.ZERO;
    private BigInteger winQty;
    private Integer status = 1;
    private Integer pairStatus = 0;
    private BigDecimal seniorCommission = BigDecimal.ZERO;
    private BigDecimal seniorRebateRate = BigDecimal.ZERO;
    private BigDecimal superSeniorCommission = BigDecimal.ZERO;
    private BigDecimal superSeniorRebateRate = BigDecimal.ZERO;
    private BigDecimal masterCommission = BigDecimal.ZERO;
    private BigDecimal masterRebateRate = BigDecimal.ZERO;

    public Integer getSectionNumber() {
        if (sectionNumber == null)
            return 1;
        return sectionNumber;
    }

    public BigInteger getWinQty() {
        if(winQty == null)
            return BigInteger.ZERO;
        return winQty;
    }

    public OrderItemsDTO() {
    }

    public OrderItemsDTO(VNOneTempOrderItemsEntity item) {
        BeanUtils.copyProperties(item, this);
        this.id = BigInteger.valueOf(item.getId());
    }

    public OrderItemsDTO(VNTwoTempOrderItemsEntity item) {
        BeanUtils.copyProperties(item, this);
        this.id = BigInteger.valueOf(item.getId());
    }

    public OrderItemsDTO(LeapTempOrderItemsEntity item) {
        BeanUtils.copyProperties(item, this);
        this.id = BigInteger.valueOf(item.getId());
    }

    public OrderItemsDTO(TNTempOrderItemsEntity item) {
        BeanUtils.copyProperties(item, this);
        this.id = BigInteger.valueOf(item.getId());
    }

    public OrderItemsDTO(KHTempOrderItemsEntity item) {
        BeanUtils.copyProperties(item, this);
        this.id = BigInteger.valueOf(item.getId());
    }

    public OrderItemsDTO(SCTempOrderItemsEntity item) {
        BeanUtils.copyProperties(item, this);
        this.id = BigInteger.valueOf(item.getId());
    }

    public OrderItemsDTO(THTempOrderItemsEntity item) {
        BeanUtils.copyProperties(item, this);
        this.id = BigInteger.valueOf(item.getId());
        this.posts = "";
    }
}
