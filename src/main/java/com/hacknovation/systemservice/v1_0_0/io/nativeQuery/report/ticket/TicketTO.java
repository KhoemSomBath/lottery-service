package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.ticket;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class TicketTO {
    private BigInteger id;
    private String userCode;
    private String drawCode;
    private Date expiredAt;
    private String result;
    private String underOverCode;
    private String rangeCode;
    private Integer multiple;
    private Boolean status;
    private Date createdAt;
}