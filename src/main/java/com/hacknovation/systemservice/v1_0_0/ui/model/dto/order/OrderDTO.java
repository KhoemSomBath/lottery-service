package com.hacknovation.systemservice.v1_0_0.ui.model.dto.order;

import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempOrdersEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempOrdersEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigInteger;
import java.util.Date;

@Data
public class OrderDTO {
    private BigInteger id;
    private String userCode;
    private String ticketNumber;
    private Integer pageNumber;
    private Integer columnNumber;
    private String drawCode;
    private Date drawAt;
    private Date createdAt;
    private String platformName;
    private String platformType;
    private String deviceName;
    private String createdBy;
    private String updatedBy;
    private Byte status;
    private Integer hasLotteryId;
    private Boolean isSeen;
    private Boolean isMark;
    public OrderDTO() {}

    public OrderDTO(VNOneTempOrdersEntity order) {
        BeanUtils.copyProperties(order, this);
        this.id = BigInteger.valueOf(order.getId());
        this.status = order.getStatus().byteValue();
    }

    public OrderDTO(VNTwoTempOrdersEntity order) {
        BeanUtils.copyProperties(order, this);
        this.id = BigInteger.valueOf(order.getId());
        this.status = order.getStatus().byteValue();
    }

    public OrderDTO(LeapTempOrdersEntity order) {
        BeanUtils.copyProperties(order, this);
        this.id = BigInteger.valueOf(order.getId());
        this.status = order.getStatus().byteValue();
    }

    public OrderDTO(KHTempOrdersEntity order) {
        BeanUtils.copyProperties(order, this);
        this.id = BigInteger.valueOf(order.getId());
        this.status = order.getStatus().byteValue();
    }

    public OrderDTO(TNTempOrdersEntity order) {
        BeanUtils.copyProperties(order, this);
        this.id = BigInteger.valueOf(order.getId());
        this.status = order.getStatus().byteValue();
    }

    public OrderDTO(SCTempOrdersEntity order) {
        BeanUtils.copyProperties(order, this);
        this.id = BigInteger.valueOf(order.getId());
        this.status = order.getStatus().byteValue();

    }

    public OrderDTO(THTempOrdersEntity order) {
        BeanUtils.copyProperties(order, this);
        this.id = BigInteger.valueOf(order.getId());
        this.status = order.getStatus().byteValue();
    }

}
