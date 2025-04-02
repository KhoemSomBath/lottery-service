package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.finance;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
public class FinanceListTO {
    private BigInteger id;
    private String code;
    private String userCode;
    private String proceedBy;
    private String proceedByUsername;
    private String proceedByNickname;
    private BigDecimal amount;
    private String currencyCode;
    private String remark;
    private Object type;
    private Object status;
    private Date createdAt;
    private Date updatedAt;
}