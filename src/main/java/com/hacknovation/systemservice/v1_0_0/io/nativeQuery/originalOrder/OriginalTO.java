package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.originalOrder;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
public class OriginalTO {
    private BigInteger originalId;
    private String userCode;
    private String drawCode;
    private Date drawAt;
    private Integer pageNumber;
    private String posts;
    private String currencyCode;
    private String rebateCode;
    private BigDecimal betAmount;
    private BigDecimal totalAmount;
    private String numberDetail;
    private Integer numberQuantity;

    public String getPosts() {
        if (this.posts == null) {
            return "";
        }
        return posts.replaceAll("LoABCD", "Lo");
    }
}
